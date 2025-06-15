package ayds.songinfo.moredetails.domain

import ayds.songinfo.moredetails.domain.ArtistBiography

interface OtherInfoRepository {
    fun getArtistBiography(artistName: String): ArtistBiography
}