package night.lines.todo.toothpick.module

import android.arch.persistence.room.Room
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import night.lines.todo.data.database.db.AppDatabase
import night.lines.todo.data.database.db.converter.DatabaseConverter
import night.lines.todo.domain.interactor.main.AddTaskIdUseCase
import night.lines.todo.domain.interactor.main.GetTaskIdListUseCase
import night.lines.todo.domain.interactor.main.GetTaskListIdUseCase
import night.lines.todo.domain.interactor.main.SetTaskListIdUseCase
import night.lines.todo.domain.repository.DatabaseRepository
import night.lines.todo.domain.repository.PreferencesRepository
import night.lines.todo.toothpick.main.navigation.provider.AddTaskIdUseCaseProvider
import night.lines.todo.toothpick.main.provider.GetTaskIdListUseCaseProvider
import night.lines.todo.toothpick.main.provider.GetTaskListIdUseCaseProvider
import night.lines.todo.toothpick.main.provider.SetTaskListIdUseCaseProvider
import night.lines.todo.toothpick.provider.DatabaseConverterProvider
import night.lines.todo.toothpick.provider.DatabaseRepositoryProvider
import night.lines.todo.toothpick.provider.LinearLayoutManagerProvider
import night.lines.todo.toothpick.provider.PreferencesRepositoryProvider
import night.lines.todo.util.SchedulerProvider
import night.lines.todo.util.SchedulerProviderImpl
import toothpick.config.Module

/**
 * Created by denisgabyshev on 19/03/2018.
 */
class ApplicationModule(context: Context) : Module() {
    init {
        bind(Context::class.java).toInstance(context)
        bind(AppDatabase::class.java).toInstance(Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "todo.db").build()
        )
        bind(DatabaseConverter::class.java).toProvider(DatabaseConverterProvider::class.java)
        bind(DatabaseRepository::class.java).toProvider(DatabaseRepositoryProvider::class.java).providesSingletonInScope()
        bind(SchedulerProvider::class.java).toInstance(SchedulerProviderImpl())
        bind(PreferencesRepository::class.java).toProvider(PreferencesRepositoryProvider::class.java).providesSingletonInScope()
        bind(LinearLayoutManager::class.java).toProvider(LinearLayoutManagerProvider::class.java)
        bind(GetTaskIdListUseCase::class.java).toProvider(GetTaskIdListUseCaseProvider::class.java).providesSingletonInScope()
        bind(AddTaskIdUseCase::class.java).toProvider(AddTaskIdUseCaseProvider::class.java).providesSingletonInScope()

        //tegorov
    }
}