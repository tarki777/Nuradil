package com.ktor.services

import com.ktor.models.Student
import com.ktor.models.Students
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq 
import org.jetbrains.exposed.sql.transactions.transaction


 ///Сервис для выполнения CRUD-операций со студентами

class StudentService {
    
    fun getAllStudents(minAge: Int?): List<Student> = transaction {
        val query = Students.selectAll()

        val filteredQuery = if (minAge != null) { 
            query.andWhere { Students.age greaterEq minAge } 
        } else {
            query
        }
        
        filteredQuery.map { 

            Student(it[Students.id].value, it[Students.name], it[Students.age]) 
        }
    }

    fun getStudentById(id: Int): Student? = transaction {

        Students.select { Students.id eq id } 
            .map { Student(it[Students.id].value, it[Students.name], it[Students.age]) }
            .singleOrNull()
    }

    fun createStudent(student: Student): Student = transaction {

        val newId = Students.insertAndGetId {
            it[Students.name] = student.name
            it[Students.age] = student.age
        }.value 
        student.copy(id = newId)
    }

    fun deleteStudent(id: Int): Boolean = transaction {

        Students.deleteWhere { Students.id eq id } > 0 
    }
}