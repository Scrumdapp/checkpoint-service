package com.scrumdapp.checkpointservice.utils.validators

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [FeatureValidRegexValidator::class])
annotation class FeatureValidRegex(
    val message: String = "Invalid regex",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class FeatureValidRegexValidator: ConstraintValidator<FeatureValidRegex, String> {
    override fun isValid(value: String?, ctx: ConstraintValidatorContext?): Boolean {
        val regex = """"[a-z0-9]\.[a-z0-9]+(?:_[a-z0-9]+)*$""".toRegex()
        return regex.containsMatchIn(value.orEmpty())
    }

}