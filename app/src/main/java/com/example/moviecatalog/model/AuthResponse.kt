package com.example.moviecatalog.model

data class AuthResponse(
    val message: String? = null,
    val errors: Errors? = null,
    val token: String? = null
)

data class Errors(
    val duplicateUserName: DuplicateUserName? = null
)

data class ErrorsItem(
    val exception: Any? = null,
    val errorMessage: String? = null
)

data class DuplicateUserName(
    val isContainerNode: Boolean? = null,
    val rawValue: Any? = null,
    val children: Any? = null,
    val attemptedValue: Any? = null,
    val errors: List<ErrorsItem?>? = null,
    val validationState: Int? = null
)