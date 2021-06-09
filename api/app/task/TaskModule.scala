package task

import play.api.inject.SimpleModule
import play.api.inject._

/**
 * Start Module when application starts
 */
class TaskModule extends SimpleModule(bind[DataSourceTask].toSelf.eagerly())