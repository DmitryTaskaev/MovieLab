package com.example.movielib

data class Movie(
	val image: Image? = null,
	val titleType: String? = null,
	val year: Int? = null,
	val type: String? = null,
	val id: String? = null,
	val title: String? = null
)

data class Image(
	val width: Int? = null,
	val id: String? = null,
	val url: String? = null,
	val height: Int? = null
)

