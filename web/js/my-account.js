function loadData() {
    getUserData();
    getCityData();
    loadUserOrders();
    loadUserProducts();
}

async function getUserData() {
    const response = await fetch("MyAccount");
    if (response.ok) {
        const json = await response.json();
        document.getElementById("username").innerHTML = `Hello, ${json.firstName} ${json.lastName}`;
        document.getElementById("since").innerHTML = `Smart Trade Member Since ${json.since}`;
        document.getElementById("firstName").value = json.firstName;
        document.getElementById("lastName").value = json.lastName;
        document.getElementById("currentPassword").value = json.password;

        if (json.hasOwnProperty("addressList") && json.addressList !== undefined) {
            let email;
            let lineOne;
            let lineTwo;
            let city;
            let postalCode;
            let cityId;
            let mobile;
            const addressUL = document.getElementById("addressUL");
            json.addressList.forEach(address => {
                email = address.user.email;
                lineOne = address.lineOne;
                lineTwo = address.lineTwo;
                city = address.city.name;
                postalCode = address.postalCode;
                cityId = address.city.id;
                mobile = address.mobile;
                const line = document.createElement("li");
                line.innerHTML = lineOne + ",<br/>" +
                        lineTwo + ",<br/>" +
                        city + "<br/>" +
                        postalCode;
                addressUL.appendChild(line);
            });
            document.getElementById("addName").innerHTML = `Name: ${json.firstName} ${json.lastName}`;
            document.getElementById("addEmail").innerHTML = `Email: ${email}`;
            document.getElementById("contact").innerHTML = `Phone: ${mobile}`;

            document.getElementById("lineOne").value = lineOne;
            document.getElementById("lineTwo").value = lineTwo;
            document.getElementById("postalCode").value = postalCode;
            document.getElementById("citySelect").value = parseInt(cityId);
            document.getElementById("mobile").value = mobile;
        }
    }

}


async function getCityData() {
    const response = await fetch("CityData");
    if (response.ok) {
        const json = await response.json();
        const citySelect = document.getElementById("citySelect");
        json.forEach(city => {
            let option = document.createElement("option");
            option.innerHTML = city.name;
            option.value = city.id;
            citySelect.appendChild(option);
        });
    }
}

async function saveChanges() {

    const popup = new Notification();

    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const lineOne = document.getElementById("lineOne").value;
    const lineTwo = document.getElementById("lineTwo").value;
    const postalCode = document.getElementById("postalCode").value;
    const cityId = document.getElementById("citySelect").value;
    const mobile = document.getElementById("mobile").value;
    const currentPassword = document.getElementById("currentPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    const userDataObject = {
        firstName: firstName,
        lastName: lastName,
        lineOne: lineOne,
        lineTwo: lineTwo,
        postalCode: postalCode,
        cityId: cityId,
        mobile: mobile,
        currentPassword: currentPassword,
        newPassword: newPassword,
        confirmPassword: confirmPassword
    };

    const userDataJSON = JSON.stringify(userDataObject);
    const response = await fetch("MyAccount", {
        method: "PUT",
        header: {
            "Content-Type": "application/json"
        },
        body: userDataJSON
    });

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            getUserData();
        } else {
            popup.error({
                message: json.message
            });
        }
    } else {
        popup.success({
            message: "Prrofile details update Failed!"
        });
    }
     location.reload();
}

async function loadUserOrders() {
    const response = await fetch("GetUserOrders");
    const orders = await response.json();

    const tbody = document.getElementById("order-table-body");
    tbody.innerHTML = "";

    if (orders.length === 0) {
        tbody.innerHTML = "<tr><td colspan='6'>No orders found.</td></tr>";
        return;
    }

    for (let order of orders) {
        const row = `
            <tr>
                <th scope="row">#${order.id}</th>
                <td>${order.title}</td>
                <td>Rs ${Number(order.price).toFixed(2)}</td>
                <td>${order.quantity}</td>
                <td>${order.date}</td>
                <td><a href="#" class="axil-btn view-btn">View</a></td>
            </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", row);
    }
}

async function loadUserProducts() {
    const response = await fetch("GetUserProducts");
    const products = await response.json();

    const tbody = document.getElementById("product-table-body");
    tbody.innerHTML = "";

    if (products.length === 0) {
        tbody.innerHTML = "<tr><td colspan='6'>No products found.</td></tr>";
        return;
    }

    for (let product of products) {
        const row = `
            <tr>
                <th scope="row">#${product.id}</th>
                <td>${product.title}</td>
                <td>Rs ${Number(product.price).toFixed(2)}</td>
                <td>${product.quantity}</td>
                <td>${product.date}</td>
                <td><a href="#" class="axil-btn view-btn">Edit</a></td>
            </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", row);
    }
}

