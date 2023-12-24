package com.igor_shaula.api_polling.data_layer.stub_source

import com.igor_shaula.api_polling.data_layer.DEFAULT_POLLING_INTERVAL
import com.igor_shaula.api_polling.data_layer.JavaTPEBasedPollingEngine
import com.igor_shaula.api_polling.data_layer.PollingEngine
import com.igor_shaula.api_polling.data_layer.TheRepository
import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.VehicleStatus
import com.igor_shaula.api_polling.data_layer.detectNumberOfNearVehicles
import com.igor_shaula.api_polling.data_layer.toVehicleItemRecords
import com.igor_shaula.api_polling.data_layer.toVehicleRecordList
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

class TheRepositoryStubImpl : TheRepository {

    private var pollingEngine: PollingEngine? = null

    private val coroutineScope = MainScope() + CoroutineName(this.javaClass.simpleName)

    override suspend fun getAllVehiclesIds(): MutableMap<String, VehicleStatus> =
        readVehiclesList()
            .associateBy({ it.vehicleId }, { it.vehicleStatus })
            .toMutableMap()

    override suspend fun startGettingVehiclesDetails(
        vehiclesMap: MutableMap<String, VehicleStatus>?,
        updateViewModel: (Pair<String, VehicleDetailsRecord>) -> Unit
    ) {
        vehiclesMap?.let {
            preparePollingEngine(it.size)
            vehiclesMap.forEach { (key, _) ->
                pollingEngine?.launch(DEFAULT_POLLING_INTERVAL) {
                    coroutineScope.launch {
                        getAllDetailsForOneVehicle(key, updateViewModel)
                    }
                }
            }
        }
    }

    override fun stopGettingVehiclesDetails() {
        pollingEngine?.stopAndClearItself()
        coroutineScope.cancel()
    }

    override fun getNumberOfNearVehicles(vehiclesMap: MutableMap<String, VehicleStatus>?): Int {
        val vehicleRecordsList = vehiclesMap?.toList()?.toVehicleRecordList()
        return detectNumberOfNearVehicles(vehicleRecordsList)
    }

    private suspend fun readVehiclesList(): List<VehicleRecord> {
        val vehicleDataStubService = StubDataSource()
        Timber.v("readVehiclesList: created vehicleDataStubService: ${vehicleDataStubService.hashCode()}")
        val vehicleList = vehicleDataStubService.getVehiclesList()
        Timber.v("readVehiclesList: received vehicleList: $vehicleList")
        return vehicleList.toVehicleItemRecords()
    }

    private fun preparePollingEngine(size: Int) {
        pollingEngine = JavaTPEBasedPollingEngine.prepare(size)
    }

    private suspend fun getAllDetailsForOneVehicle(
        vehicleId: String, updateViewModel: (Pair<String, VehicleDetailsRecord>) -> Unit
    ) {
        val vehicleDetails = readVehicleDetails(vehicleId)
        Timber.d("vehicleDetails = $vehicleDetails")
        updateViewModel(Pair(vehicleId, vehicleDetails))
    }

    private suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord {
        val vehicleDataStubService = StubDataSource()
        Timber.v("readVehiclesList: created vehicleDataStubService: ${vehicleDataStubService.hashCode()}")
        val vehicleDetails = vehicleDataStubService.getVehicleDetails(vehicleId)
        Timber.v("readVehiclesList: received vehicleDetails: $vehicleDetails")
        return vehicleDetails.toVehicleItemRecords()
    }
}