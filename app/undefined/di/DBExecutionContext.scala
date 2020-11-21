package undefined.di

import com.google.inject.ImplementedBy

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@ImplementedBy(classOf[DBExecutionContextImpl])
trait DBExecutionContext extends ExecutionContext
