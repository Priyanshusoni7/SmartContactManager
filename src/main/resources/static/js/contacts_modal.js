const baseURL = "http://localhost:8081"

const viewContactModel = document.getElementById("view_contact_modal");

// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
    id: 'view_contact_modal',
    override: true,
};

const contactModal = new Modal(viewContactModel, options, instanceOptions);

function openContactModal() {

    contactModal.show();
}

function closeContactModal() {
    contactModal.hide();
}

async function loadContactdata(id) {
    //id coming from view-comtacts.html
    console.log(id);

    try {
        const data = await (await fetch(`${baseURL}/api/contacts/${id}`)).json();

        console.log(data);
        document.querySelector('#contact_modal_name').innerHTML = data.name;
        document.querySelector('#contact_modal_email').innerHTML = data.email;
        document.querySelector('#contact_modal_number').innerHTML = data.phoneNumber;
        document.querySelector('#contact_modal_image').src = data.picture;
        document.querySelector('#contact_modal_address').innerHTML = data.address;
        document.querySelector('#contact_modal_description').innerHTML = data.description;
        const favorite = document.querySelector('#contact_modal_favorite');

        if (favorite) {
            favorite.innerHTML = "This is your Favorite Contact";
        }
        else {
            favorite.innerHTML = "This is your not favorite contact";
        }

        document.querySelector('#contact_modal_linkedIn').href = data.linkedin ? data.linkedin : " ";
        document.querySelector('#contact_modal_linkedIn').innerHTML = data.linkedin ? data.linkedin : "N/A";

        document.querySelector('#contact_modal_website').innerHTML = data.websiteLink ? data.websiteLink : "N/A";
        document.querySelector('#contact_modal_website').href = data.websiteLink ? data.websiteLink : " ";


        openContactModal();
    } catch (error) {
        console.log("Error: ", error);
    }
}

// delete contact
async function deleteContact(id) {
    console.log("delete contact " + id);

    Swal.fire({
        title: "Do you want to Delete the Contact?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Delete",
        confirmButtonColor: "#d33",
        cancelButtonColor: "#3085d6",
    }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
            const url = `${baseURL}/user/contacts/delete/${id}`;

            // this will hit the api/url for delete
            window.location.replace(url);
        }
    });
}