function indexOnloadFunctions() {
    checkSessionCart();
    loadProductData();
}
async function checkSessionCart() {
    const popup = new Notification();
    const response = await fetch("CheckSessionCart");
    if (!response.ok) {
        popup.error({
            message: "Something went wrong! Try again shortly"
        });
    }
}

async function loadProductData() {

    const popup = new Notification();
    const response = await fetch("LoadHomeData");
    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            loadNewArrivals(json);
        } else {
            popup.error({
                message: "Something went wrong! Try again shortly"
            });
        }
    } else {
        popup.error({
            message: "Something went wrong! Try again shortly"
        });
    }
}
function loadNewArrivals(json) {
    const new_arrival_product_container = document.getElementById("new-arrival-product-container");
    new_arrival_product_container.innerHTML = "";

    json.productList.forEach(item => {
        let product_card = `   <div class="col-xl-3 col-lg-4 col-sm-6 col-12 mb--30">
                                        <div class="axil-product product-style-one">
                                            <div class="thumbnail">
                                                <a href="single-product.html?id=${item.id}">
                                                    <img data-sal-delay="200" data-sal-duration="800" loading="lazy" class="main-img" src="product-images\\${item.id}\\image1.png" alt="Product Images">
                                                </a>

                                                <div class="product-hover-action">
                                                    <ul class="cart-action">
                                                        <li class="quickview"><a href="single-product.html?id=${item.id}"><i class="far fa-eye"></i></a></li>
                                                        <li class="select-option"><a href="#" onclick="event.preventDefault(); addToCart(${item.id}, 1);">Add To Cart</a></li>
                                                        <li class="wishlist"><a href="#"><i class="far fa-heart"></i></a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="product-content">
                                                <div class="inner">
                                                    <h5 class="title"><a href="#">${item.title}</a></h5>
                                                    <div class="product-price-variant">
                                                        <span class="price current-price">Rs. ${new Intl.NumberFormat(
                "en-US",
                {minimumFractionDigits: 2})
                .format(item.price)}</span>
                                                    </div>
                                                    <div class="color-variant-wrapper">
                                                        <ul class="color-variant">
                                                            <li class="color-extra-01 active"><span style="border-color:black;">
<span class="color" style="background-color:${item.color.value};"></span></span>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>`;
        new_arrival_product_container.innerHTML += product_card;
    });
}

async function addToCart(productId, qty) {
    const popup = new Notification();// link notification js in single-product.html
    const response = await fetch("AddToCart?prId=" + productId + "&qty=" + qty);
    if (response.ok) {
        const json = await response.json(); // await response.text();
        if (json.status) {
            popup.success({
                message: json.message
            });
        } else {
            popup.error({
                message: "Something went wrong. Try again"
            });

        }
    } else {
        popup.error({
            message: "Something went wrong. Try again"
        });
    }
}

