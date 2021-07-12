package ru.androidschool.appsearch.data

import android.util.Log
import androidx.appsearch.app.AppSearchBatchResult
import androidx.appsearch.app.AppSearchSession
import androidx.appsearch.app.GenericDocument
import androidx.appsearch.app.PutDocumentsRequest
import androidx.appsearch.app.SearchResult
import androidx.appsearch.app.SearchResults
import androidx.appsearch.app.SearchSpec
import androidx.appsearch.app.SetSchemaRequest
import androidx.appsearch.app.SetSchemaResponse
import androidx.appsearch.exceptions.AppSearchException
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import io.reactivex.Observable
import java.util.concurrent.ExecutorService

@Suppress("UnstableApiUsage")
class AppSearchRepository(
    private val sessionFuture: ListenableFuture<AppSearchSession>,
    private val threadPoolExecutor: ExecutorService
) {

    private val TAG = AppSearchRepository::class.simpleName

    // Set the Scheme
    fun setSchema(documentClasses: Collection<Class<*>>) {
        val setSchemaRequest =
            SetSchemaRequest.Builder().addDocumentClasses(documentClasses)
                .build()

       // Created ListenableFuture
        val setSchemaFuture = Futures.transformAsync(
            sessionFuture,
            { session ->
                session?.setSchema(setSchemaRequest)
            }, threadPoolExecutor
        )

        Futures.addCallback(
            setSchemaFuture,
            object : FutureCallback<SetSchemaResponse> {
                override fun onSuccess(result: SetSchemaResponse?) {
                    Log.d(TAG, "SetSchemaResponse success. $result")
                }

                override fun onFailure(t: Throwable) {
                    Log.d(TAG, "Failed to put documents. $t")
                }
            },
            threadPoolExecutor
        )
    }

    fun saveData(documents: List<Any>): Observable<Boolean> {
        val putRequest = PutDocumentsRequest.Builder().addDocuments(documents).build()
        val putFuture = Futures.transformAsync(
            sessionFuture,
            { session ->
                session?.put(putRequest)
            }, threadPoolExecutor
        )
        return Observable.create { emitter ->
            Futures.addCallback(
                putFuture,
                object : FutureCallback<AppSearchBatchResult<String, Void>?> {
                    override fun onSuccess(result: AppSearchBatchResult<String, Void>?) {

                        // Gets map of successful results from Id to Void
                        val successfulResults = result?.successes
                        Log.d(TAG, "successfulResults" + result?.successes)

                        // Gets map of failed results from Id to AppSearchResult
                        val failedResults = result?.failures
                        Log.d(TAG, "failedResults" + result?.failures)
                        emitter.onNext(true)
                    }

                    override fun onFailure(t: Throwable) {
                        Log.e(TAG, "Failed to put documents. $t")
                        emitter.onError(t)
                    }
                },
                threadPoolExecutor
            )
        }
    }

    fun search(term: String, nameSpace: String? = ""): Observable<SearchResults> {
        Log.d(TAG, "search: $term")
        val searchSpec = SearchSpec.Builder()
            .addFilterNamespaces(nameSpace)
            .build();

        val searchFuture: ListenableFuture<SearchResults> = Futures.transform(
            sessionFuture,
            { session ->
                session?.search(term, searchSpec)
            },
            threadPoolExecutor
        )

        return Observable.create { emitter ->
            Futures.addCallback(
                searchFuture,
                object : FutureCallback<SearchResults> {
                    override fun onSuccess(searchResults: SearchResults?) {
                        searchResults?.let { emitter.onNext(searchResults) }
                    }

                    override fun onFailure(t: Throwable) {
                        emitter.onError(t)
                    }
                },
                threadPoolExecutor
            )
        }
    }

    fun iterateSearchResults(
        searchResults: SearchResults?
    ): Observable<List<GenericDocument>> {

        val getDocumentsFuture: ListenableFuture<List<GenericDocument>> = Futures.transform(
            searchResults?.nextPage,
            { page: List<SearchResult>? ->
                // Gets GenericDocument from SearchResult.
                val genericDocument: List<GenericDocument>? =
                    page?.map { it.genericDocument }?.toList()

                genericDocument
            },
            threadPoolExecutor
        )

        return Observable.create { emitter ->
            Futures.addCallback(
                getDocumentsFuture,
                object : FutureCallback<List<GenericDocument>> {

                    override fun onSuccess(result: List<GenericDocument>?) {
                        result?.let {
                            Log.d(TAG, "onSuccess $result")
                            emitter.onNext(result)
                        }
                    }

                    override fun onFailure(t: Throwable) {
                        Log.d(TAG, "onFailure $t")
                        emitter.onError(t)
                    }
                },
                threadPoolExecutor
            )
        }
    }
}