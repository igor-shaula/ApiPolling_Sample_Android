package com.igor_shaula.api_polling.data_layer

import com.igor_shaula.api_polling.data_layer.network.VehicleDetailsModel
import com.igor_shaula.api_polling.data_layer.network.VehicleModel

fun List<Pair<String, VehicleRecord>>.toVehicleRecordList(): List<VehicleRecord> {
    val result = mutableListOf<VehicleRecord>()
    this.forEach {
        result.add(it.second)
    }
    return result
}

fun List<VehicleModel>?.toVehicleItemRecords(): List<VehicleRecord> {
    val result = mutableListOf<VehicleRecord>()
    this?.forEach {
        result.add(VehicleRecord(vehicleId = it.vehicleId, vehicleStatus = VehicleStatus.NEW_ROUND))
    }
    return result
}

fun VehicleDetailsModel.toVehicleItemRecords(): VehicleDetailsRecord =
    VehicleDetailsRecord(
        vehicleId = this.vehicleId,
        latitude = this.location.latitude,
        longitude = this.location.longitude
    )
