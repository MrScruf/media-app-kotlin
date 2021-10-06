package net.krupizde.mediaApp.persistence.entity

import java.math.BigInteger
import javax.persistence.*

@MappedSuperclass
data class MyEntity(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?
) {
}

@Entity
class Image(
    @Column var name: String,
    @Column var path: String?,
    @Column(name = "phash") var pHash: BigInteger?,
    @ManyToOne @JoinColumn(name = "id_album") var album: Album?,
    id: Long? = null
) : MyEntity(id) {
}

@Entity
class Person(
    @Column var name: String,
    @OneToMany(mappedBy = "person") var albums: MutableList<Album> = mutableListOf(),
    id: Long?
) : MyEntity(id) {
}

@Entity
class Album(
    @Column var name: String,
    @ManyToOne @JoinColumn(name = "id_person") var person: Person?,
    @OneToMany(mappedBy = "album") var images: MutableList<Image> = mutableListOf(),
    id: Long? = null
) : MyEntity(id) {
}

@Entity
@Table(name = "AddedDirectories")
class Directory(@Column val path: String, id: Long? = null) : MyEntity(id){
}

