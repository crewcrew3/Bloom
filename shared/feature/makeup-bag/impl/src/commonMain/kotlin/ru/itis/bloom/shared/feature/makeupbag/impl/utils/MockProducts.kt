package ru.itis.bloom.shared.feature.makeupbag.impl.utils

import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductStatus

object MockProducts {
    val sampleProducts: List<Product> = listOf(
        // 1. Очищение (Active, с рейтингом и отзывом)
        Product(
            id = "prod_001",
            userId = "user_123",
            name = "Hydrating Facial Cleanser",
            brand = "CeraVe",
            category = ProductCategory.Cleanser,
            inciComposition = "Aqua, Glycerin, Ceteth-20, Cetearyl Alcohol, Phenoxyethanol, Aqua, Glycerin, Ceteth-20, Cetearyl Alcohol, Phenoxyethanol...",
            personalRating = 5,
            personalReview = "Отлично увлажняет, не стягивает кожу. Идеально для утра.",
            photoUrl = "https://avatars.mds.yandex.net/i?id=2b336af39e9492bbea285c77f9916386_l-12428376-images-thumbs&n=13",
            openedDate = "2024-05-10",
            shelfLifeAfterOpening = 12,
            expiryDate = "2025-05-10",
            status = ProductStatus.Active,
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-05-10T14:20:00Z"
        ),
        // 2. Сыворотка (Active, без фото)
        Product(
            id = "prod_002",
            userId = "user_123",
            name = "Niacinamide 10% + Zinc 1%",
            brand = "The Ordinary",
            category = ProductCategory.Serum,
            inciComposition = "Aqua, Niacinamide, Pentylene Glycol, Zinc PCA...",
            personalRating = 4,
            personalReview = null,
            photoUrl = null,
            openedDate = "2024-08-20",
            shelfLifeAfterOpening = 6,
            expiryDate = "2025-02-20",
            status = ProductStatus.Active,
            createdAt = "2024-08-20T09:00:00Z",
            updatedAt = "2024-08-20T09:00:00Z"
        ),
        // 3. SPF (Active, длинный INCI)
        Product(
            id = "prod_003",
            userId = "user_123",
            name = "Anthelios UVMune 400 Invisible Fluid",
            brand = "La Roche-Posay",
            category = ProductCategory.Sunscreen,
            inciComposition = "Aqua, Homosalate, Ethylhexyl Salicylate, Bis-Ethylhexyloxyphenol Methoxyphenyl Triazine...",
            personalRating = 5,
            personalReview = "Лучший санскрин, не жирнит и не белит.",
            photoUrl = "https://yandex-images.clstorage.net/E53s1rx34/650114s5d0G/KipW10ZWIOwaQaL1U2CaKdTdbHm2957DraAv_zBbZuTHqGucnrF1YkO8Hdzvtjivtz1Ri5C3D2cUvDx2LGMIVv77g4YRx-DtX1NgZlXjniyH2jy85nHfwnvjLTC_x3otHLdyApcu6yRDPokJfTPpfqykwUwCPp8z0XDwfLZ-kYXvbxxeQkBhZxxW7DVc5xBnhdwndaO0wYyofHp0cc_66dZ3u0uYZ-hpyPuIQvnm7X-uI1OECrlSMB13ffn-fx9AoOab2QgI6O2a0-R-1mQTpw8Qbr8vOIqK73H8O35PtmFWujZBFmlm4M83xQPxIC1-q6NbBg99X3YZPTG3tnxdCqArkJAIC-7uWlRltVCqVeqNnW-86LtDSG6-sbzxRLhkFj92CVatqacOeMDCMODvsq5qEocIOtV4nrAwcDk9UQCk6VPWzIdpIFKQbPwQJJopjJDpOGa9jQKgvXW0eEU54Js_NULcKCbgQnZEA3PlbXOo5xdLxnNfOVd0Mb96tRlN6-SfFYjL521YG64-EKNe50kbIHnodMqHqLo6vfBE9iseP3cB0uagIEE9iIB4qKDwa6lSxID8XbcQN7b28ztTw2NvndaKDyUrnxqisRuqUKmLXKj4bDcNT2B2_H74hDil2nX6SJQvraLOcUoKeq0gdekg28CPe9p4l78-NXG0WYcv5ZjSSszh5pQfZ3CWo1VmRJejd639jAGuPTr8PkY5ZdA8e4vWqOPpQTkDTTjgLbIoK5rMDHFTeFBwNfNyepMGpyEW24fFJ2kc3mo-UqDRKcxVrHBgsMSNIP46NnLNPuPT9HvMFqno7sO4iE59YO_4ZuaST4Jy2jmdejM89rueCeZklhIGgWltWheis1piEq4MHKl9bn-DDWH0O_X0gjwuUv56SF4gIOpHtYSN_SpuuW3qkQ6F8pg2HHa6v7m6HwEuY1wSR0yprlLfLvZRJNKvjZom96d3isqi9Dh8sU",
            openedDate = "2024-03-01",
            shelfLifeAfterOpening = 12,
            expiryDate = null,
            status = ProductStatus.Active,
            createdAt = "2024-03-01T12:00:00Z",
            updatedAt = "2024-03-01T12:00:00Z"
        ),
        // 4. Тонер (Archived, истёк срок)
        Product(
            id = "prod_004",
            userId = "user_123",
            name = "Witch Hazel Toner",
            brand = "Thayers",
            category = ProductCategory.Toner,
            inciComposition = "Aloe Barbadensis Leaf Juice, Witch Hazel, Glycerin...",
            personalRating = 3,
            personalReview = "Немного сушит, но в целом норм для жирной кожи.",
            photoUrl = null,
            openedDate = "2023-01-10",
            shelfLifeAfterOpening = 12,
            expiryDate = "2024-01-10",
            status = ProductStatus.Archived,
            createdAt = "2023-01-10T08:00:00Z",
            updatedAt = "2024-01-15T10:00:00Z"
        ),
        // 5. Маска (Active, минимальные данные)
        Product(
            id = "prod_005",
            userId = "user_123",
            name = "Super Volcanic Pore Clay Mask",
            brand = "Innisfree",
            category = ProductCategory.Mask,
            inciComposition = "Water, Kaolin, Glycerin, Butylene Glycol...",
            personalRating = null,
            personalReview = null,
            photoUrl = "https://avatars.mds.yandex.net/i?id=c323586af56e8fcceb9b23de402f6abc33bc59d8-6959765-images-thumbs&n=13",
            openedDate = null,
            shelfLifeAfterOpening = null,
            expiryDate = null,
            status = ProductStatus.Active,
            createdAt = "2024-06-12T15:30:00Z",
            updatedAt = "2024-06-12T15:30:00Z"
        ),
        // 6. Крем для глаз (Active, высокий рейтинг)
        Product(
            id = "prod_006",
            userId = "user_123",
            name = "Creamy Eye Treatment with Avocado",
            brand = "Kiehl's",
            category = ProductCategory.EyeCream,
            inciComposition = "Aqua, Persea Gratissima Oil, Butyrospermum Parkii Butter...",
            personalRating = 4,
            personalReview = "Питательный, хорошо убирает сухость под глазами.",
            photoUrl = null,
            openedDate = "2024-09-05",
            shelfLifeAfterOpening = 6,
            expiryDate = "2025-03-05",
            status = ProductStatus.Active,
            createdAt = "2024-09-05T11:00:00Z",
            updatedAt = "2024-09-05T11:00:00Z"
        ),
        // 7. Масло (Archived, закончилось)
        Product(
            id = "prod_007",
            userId = "user_123",
            name = "100% Plant-Derived Squalane",
            brand = "The Ordinary",
            category = ProductCategory.Oil,
            inciComposition = "Squalane",
            personalRating = 5,
            personalReview = "Универсальное, использую даже для волос.",
            photoUrl = "https://avatars.mds.yandex.net/get-mpic/5232049/2a0000018d466ea31fc58ca2f0a80ca2ad15/orig",
            openedDate = "2023-11-15",
            shelfLifeAfterOpening = 12,
            expiryDate = "2024-11-15",
            status = ProductStatus.Archived,
            createdAt = "2023-11-15T09:00:00Z",
            updatedAt = "2024-11-20T10:00:00Z"
        )
    )

    fun getByCategory(category: ProductCategory?): List<Product> {
        return if (category == null) {
            sampleProducts
        } else {
            sampleProducts.filter { it.category == category }
        }
    }

    fun getById(id: String): Product? {
        return sampleProducts.find {
            it.id == id
        }
    }
}