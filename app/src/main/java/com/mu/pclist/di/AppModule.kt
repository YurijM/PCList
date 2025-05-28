package com.mu.pclist.di

import android.app.Application
import androidx.room.Room
import com.mu.pclist.data.PCDb
import com.mu.pclist.data.repository.OfficeRepositoryImpl
import com.mu.pclist.data.repository.PCRepositoryImpl
import com.mu.pclist.data.repository.UserRepositoryImpl
import com.mu.pclist.domain.repository.OfficeRepository
import com.mu.pclist.domain.repository.PCRepository
import com.mu.pclist.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRusLottoDb(app: Application): PCDb {
        return Room.databaseBuilder(
            app,
            PCDb::class.java,
            "db_pc"
        )
            .addMigrations(
                //migration_1_2,
                //migration_2_3
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideOfficeRepository(db: PCDb): OfficeRepository {
        return OfficeRepositoryImpl(db.officeDao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: PCDb): UserRepository {
        return UserRepositoryImpl(db.userDao)
    }

    @Provides
    @Singleton
    fun providePCRepository(db: PCDb): PCRepository {
        return PCRepositoryImpl(db.pcDao)
    }
}