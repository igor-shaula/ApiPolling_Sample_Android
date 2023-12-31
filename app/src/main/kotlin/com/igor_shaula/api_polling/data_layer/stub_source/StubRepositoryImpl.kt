package com.igor_shaula.api_polling.data_layer.stub_source

import com.igor_shaula.api_polling.data_layer.AbstractVehiclesRepository
import com.igor_shaula.api_polling.data_layer.VehicleDetailsRecord
import com.igor_shaula.api_polling.data_layer.VehicleRecord
import com.igor_shaula.api_polling.data_layer.toVehicleItemRecords

class StubRepositoryImpl : AbstractVehiclesRepository() {

    private val stubDataSource = StubDataSource()

    override suspend fun readVehiclesList(): List<VehicleRecord> =
        stubDataSource.getVehiclesList().toVehicleItemRecords()

    override suspend fun readVehicleDetails(vehicleId: String): VehicleDetailsRecord =
        stubDataSource.getVehicleDetails(vehicleId).toVehicleItemRecords()
}
