package com.example.nikestore.data.repo

import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.source.ProductDataSource
import com.example.nikestore.data.repo.source.ProductLocalDataSource
import io.reactivex.Completable
import io.reactivex.Single

class ProductRepositoryImpl(
    private val remoteDataSource: ProductDataSource,
    private val localDataSource: ProductLocalDataSource
) : ProductRepository {
    override fun getProducts(sort: Int): Single<List<Product>> =
        localDataSource.getFavoriteProducts()
            .flatMap { favoriteProducts ->
                remoteDataSource.getProducts(sort).doOnSuccess {
                    val favoriteProductIds = favoriteProducts.map {
                        it.id
                    }
                    it.forEach { product ->
                        if (favoriteProductIds.contains(product.id))
                            product.isFavorite = true
                    }
                }
            }

    override fun getFavoriteProducts(): Single<List<Product>> =
        localDataSource.getFavoriteProducts()

    override fun addToFavorites(product: Product): Completable =
        localDataSource.addToFavorites(product)

    override fun deleteFromFavorites(product: Product): Completable =
        localDataSource.deleteFromFavorites(product)
}