# trendyol-shopping-cart-project

Varsayımlar:

1. İndirimler sepete uygulanır. Bir sepete birden fazla indirim uygulanabilir.
2. İndirimler sepete, indirim sağlayıcılar vasıtası ile sağlanır. Her bir indirim sağlayıcı bir adet indirim sağlayabilir.
3. Bir sepete birden fazla indirim sağlayıcı, indirim uygulayabilir.
4. Aynı indirim, birden fazla indirim sağlayıcı ile ilişkilendirilebilir. Ancak sepete sadece biri uygulanır.
5. Trendyol case'i için indirim sağlayıcılar kuponlar ve kampanyalardır.
6. Kampanyalar her zaman kuponlardan önce sepete uygulanır.
7. Eğer toplam indirim tutarı, sepet tutarından büyük ise, sepet tutarı sıfırlanır, eksiye düşmez.
8. Sepete uygulanan indirimlerden, "minimum sepet tutarı" tabanlı indirimler arasından en büyük "minimum sepet tutarı" koşuluna sahip olan indirim uygulanır.
9. Sepete uygulanan indirimlerden, "kategori içerisindeki ürün sayısı" tabanlı indirimler arasından en büyük "kategori içerisindeki ürün sayısı" koşuluna sahip olan indirim uygulanır
10. Farklı kategorilere ait "kategori içerisindeki ürün sayısı" koşuluna sahip indirimler sepete uygulanabilir.
11. Oran bazlı "kategori içerisindeki ürün sayısı" indirimleri sadece sepette bulunan ilgili kategorideki ürünlerin fiyatlarına uygulanır.
12."Kategori içerisindeki ürün sayısı" indirimi uygulanırken torun kategorilerdeki ürün sayılarına da bakılmaktadır.
       

Tasarım:

1. Primitive type kullanımı yerine kendi validasyonlarınsa sahip DDD tabanlı Value Object Pattern kullanılmıştır.
Bkz. Title.java, Amount.java, Quantity.java vb...
2. Sepete bir veya birden fazla indirim sağlayıcı tarafından indirim sağlabilmesini sağlamak için ve runtime esnasında dahi bu indirim sağlayıcı gruplarını yönetebilmek adına Composite Design Pattern uygulanmıştır.
Bkz. DiscountProvider.java, DiscountProviderGroup.java, DiscountProviderItem.java
3. İndirim sağlayıcı grupları tarafından, indirim sağlandığına dair sepeti haberdar etmek için Observer Design Pattern uygulanmıştır.
Bkz. DiscountProvidedEvent, DiscountProvidedEventListener, DiscountProvider.java
4. İndirimlerin hangi koşula göre nasıl hesaplanacağının modellenmesi için Strategy Pattern kullanılmıştır.
Bkz. Discount.java, DiscountCalculationStrategy.java, DiscountValidationStrategy.java, AmounDiscountStrayegy.java, RateDiscountStrategy.java, vb...
5. Uygulanan OOP, SOLID ve Design Patterns prensiplerini göstermek amacı ile bağımlılık yönetimi desteği haricinde Spring Boot çatısına ait altyapı kullanılmamıştır.
6. Lombok kullanılmamıştır.
7. Herhangi bir veri tabanına okuma/yazma işlemleri gerçekleştirilmemiş, servis sınıfları testlerde mocklanmıştır.
8. Temel sınıfları ve altyapıları kullanan bir mock client geliştirilmiş, testleri yazılmış ve ayrı bile modül olarak "shopping-cart-client-mock" adlandırılmıştır.
9. Trendyol case'i gereksinimlerine uygun sepet "shopping-cart-client-trendyol" modülünde geliştirilmiş ve testleri yazılmıştır.

Kullanılan Teknolojiler:

* Spring Boot 2.2.7
* Java 11
* Maven 3.6.x
* Junit Jupiter
* AssertJ
* Mockito
* Jacoco Maven Plugin
* Sonar Maven Plugin
* SonarQube


Projeyi Build Etmek ve Testleri Koşmak:

1. Java 11 veya üst versiyonu gerekmektedir.
2. Maven  projesi olduğu için maven 3.6.x versiyonu gerekmektedir.
3. Proje bilgisayara indirildikten sonra "trendyol-shopping-cart-project" klasörüne girilmelidir.
4. Bu klasörde "mvn clean -U install" komutu çalıştırılarak proje build edilebilir ve birim testlere koşturulabilir.
5. Eğer http://localhost:9000/ üzerinde çalışan bir sonarqube mevcutsa "mvn clean -U install sonar:sonar" komutu ile build sonrası analiz raporu sonar'a yüklenecektir.