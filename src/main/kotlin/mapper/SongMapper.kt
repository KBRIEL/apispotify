package mapper

import org.github.unqui.Song

class SongMapper(song: Song) {

    var id:String=song.id
    var name:String=song.name
    var band:String=song.band
    var url:String=song.url
    var duration:Int=song.duration


}
