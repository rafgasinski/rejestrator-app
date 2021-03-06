package com.example.rejestrator.view.viewmodel.Admin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rejestrator.view.model.entities.EmployeeListData
import com.example.rejestrator.view.model.entities.LoginData
import com.example.rejestrator.view.model.repositories.ApiRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminLogsListViewModel(application: Application): AndroidViewModel(application)  {
    private val _allLogs: MutableLiveData<ArrayList<LoginData>> = MutableLiveData()
    val allLogs: LiveData<ArrayList<LoginData>>
        get()=_allLogs

    private val _filteredAllLogs: MutableLiveData<ArrayList<LoginData>> = MutableLiveData()
    val filteredAllLogs: LiveData<ArrayList<LoginData>>
        get()=_filteredAllLogs

    var employeeList1: ArrayList<EmployeeListData> = arrayListOf()

    var employeeList2: java.util.ArrayList<String> = arrayListOf()

    fun getAllLogs()
    {
        viewModelScope.launch {
            _allLogs.value = ApiRepository.getAllLogs()
            _filteredAllLogs.value = ApiRepository.getAllLogs()
        }
    }

    fun getAllEmployeesForTaskAdding()
    {
        ApiRepository.getAllEmployees().enqueue(object : Callback<ArrayList<EmployeeListData>>{
            override fun onFailure(call: Call<ArrayList<EmployeeListData>>, t: Throwable) {
            }

            override fun onResponse(call: Call<ArrayList<EmployeeListData>>, response: Response<ArrayList<EmployeeListData>>) {
                employeeList1 = response.body()!!
                employeeList2.clear()
                employeeList1.forEach(){
                    employeeList2.add(it.toString())
                }

            }

        })

    }

    fun insertEmployee(id : String, pin : String, name : String, surname : String, shift : String)
    {
        viewModelScope.launch {
            ApiRepository.insertEmployee(id, pin, name, surname, shift)
        }
    }

    fun insertAdmin(id : String, username: String, password: String, name : String, surname : String)
    {
        viewModelScope.launch {
            ApiRepository.insertAdmin(id, username, password, name, surname)
        }
    }
}