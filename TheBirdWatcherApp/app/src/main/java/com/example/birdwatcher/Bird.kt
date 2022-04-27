package com.example.birdwatcher

class Bird(
    id: String,
    name: String,
    rarity: Int,
    notes: String,
    image: ByteArray?,
    latLng: String?,
    address: String?,
    date: String
) {

    var id: String = id
        private set
    var name: String = name
        private set
    var rarity: Int = rarity
        private set
    var notes: String = notes
        private set
    var image: ByteArray? = image
        private set
    var latLng: String? = latLng
        private set
    var address: String? = address
        private set
    var date: String = date
        private set
}