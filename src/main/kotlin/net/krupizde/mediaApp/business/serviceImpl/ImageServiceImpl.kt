package net.krupizde.mediaApp.business.serviceImpl

import com.github.kilianB.hash.Hash
import com.github.kilianB.hashAlgorithms.PerceptiveHash
import net.krupizde.mediaApp.business.service.ImageService
import net.krupizde.mediaApp.persistence.entity.Image
import net.krupizde.mediaApp.persistence.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

@Service
class ImageServiceImpl(@Autowired repository: ImageRepository) :
    GeneralServiceImpl<Image, ImageRepository>(repository),
    ImageService {
    private val resolution = 48;
    val hasher: PerceptiveHash = PerceptiveHash(resolution)

    override fun calculatePHash(image: Image): Hash {
        val path = image.path
        path ?: throw IllegalStateException("Image ${image.id} - ${image.name} has no file path")
        return hasher.hash(File(path))
    }

    override fun compareHashes(image1: Image, image2: Image): Double {
        val hash1 = hashFromImageProperty(image1)
        val hash2 = hashFromImageProperty(image2)
        return hash1.normalizedHammingDistance(hash2)
    }

    @Transactional
    override fun compareHashes(idImage1: Long, idImage2: Long): Double {
        val image1 = repository.findById(idImage1)
        val image2 = repository.findById(idImage2)
        if (image1 == null || image2 == null)
            throw IllegalStateException("Image with id $idImage1 or $idImage2 doesnt exist")
        return compareHashes(image1, image2)
    }

    override fun hashFromImageProperty(image: Image): Hash {
        if (image.pHash == null) throw IllegalStateException("Image ${image.id} does not have a pHash")
        return Hash(image.pHash, hasher.keyResolution, hasher.algorithmId())
    }
}