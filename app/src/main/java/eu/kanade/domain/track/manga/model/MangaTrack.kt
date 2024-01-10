package eu.kanade.domain.track.manga.model

import tachiyomi.domain.track.manga.model.MangaTrack
import eu.kanade.tachiyomi.data.database.models.manga.MangaTrack as DbMangaTrack

fun MangaTrack.copyPersonalFrom(other: MangaTrack): MangaTrack {
    return this.copy(
        lastChapterRead = other.lastChapterRead,
        score = other.score,
        status = other.status,
        startDate = other.startDate,
        finishDate = other.finishDate,
    )
}

fun MangaTrack.toDbTrack(): DbMangaTrack = DbMangaTrack.create(trackerId).also {
    it.id = id
    it.manga_id = mangaId
    it.remote_id = remoteId
    it.library_id = libraryId
    it.title = title
    it.last_chapter_read = lastChapterRead.toFloat()
    it.total_chapters = totalChapters.toInt()
    it.status = status.toInt()
    it.score = score.toFloat()
    it.tracking_url = remoteUrl
    it.started_reading_date = startDate
    it.finished_reading_date = finishDate
}

fun DbMangaTrack.toDomainTrack(idRequired: Boolean = true): MangaTrack? {
    val trackId = id ?: if (idRequired.not()) -1 else return null
    return MangaTrack(
        id = trackId,
        mangaId = manga_id,
        trackerId = tracker_id.toLong(),
        remoteId = remote_id,
        libraryId = library_id,
        title = title,
        lastChapterRead = last_chapter_read.toDouble(),
        totalChapters = total_chapters.toLong(),
        status = status.toLong(),
        score = score.toDouble(),
        remoteUrl = tracking_url,
        startDate = started_reading_date,
        finishDate = finished_reading_date,
    )
}
