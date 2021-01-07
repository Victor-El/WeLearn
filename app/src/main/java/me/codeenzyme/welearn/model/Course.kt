package me.codeenzyme.welearn.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Course(

    @DocumentId
    val id: String?,

    @set:PropertyName("course-code")
    @get:PropertyName("course-code")
    var courseCode: String?,

    @set:PropertyName("course-title")
    @get:PropertyName("course-title")
    var courseTitle: String?,

    val thumbnail: String?,

    @set:PropertyName("date_created")
    @get:PropertyName("date_created")
    var dateCreated: Timestamp?,

    val school: String?,

    val topics: List<Topic>?,
): Parcelable {

    constructor() : this(null, null, null, null, null, null, null)

    @Parcelize
    data class Topic(
        val title: String?,

        @get:PropertyName("video_url")
        @set:PropertyName("video_url")
        var videoUrl: String?,

        @get:PropertyName("material_url")
        @set:PropertyName("material_url")
        var materialUrl: String?,
    ): Parcelable {
        constructor() : this(null, null, null)
    }
}
