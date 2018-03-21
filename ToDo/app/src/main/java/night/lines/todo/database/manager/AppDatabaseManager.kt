package night.lines.todo.database.manager

import android.content.Context
import io.reactivex.Flowable
import io.reactivex.Observable
import night.lines.todo.database.dao.TaskDao
import night.lines.todo.database.model.Task
import javax.inject.Inject

/**
 * Created by denisgabyshev on 18/03/2018.
 */
class AppDatabaseManager @Inject constructor(private val taskDao: TaskDao) : DatabaseManager {

    override fun insertTask(task: Task): Long = taskDao.insert(task)

    override fun getAllTasks(): Flowable<List<Task>> = taskDao.getAllTasks()

    override fun updateTask(task: Task): Observable<Unit> = Observable.fromCallable { taskDao.update(task) }

    override fun removeTask(task: Task): Observable<Unit> = Observable.fromCallable { taskDao.delete(task) }
}