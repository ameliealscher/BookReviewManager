let stars;
let clickedStarIndex = -1;

document.addEventListener("DOMContentLoaded", function () {
    stars = document.querySelectorAll(".star");
    const ratingInput = document.getElementById("rating");

    stars.forEach((star, index) => {
        star.addEventListener("mouseover", function () {
            highlightStars(index + 1);
        });

        star.addEventListener("mouseout", function () {
            highlightStars(clickedStarIndex !== -1 ? clickedStarIndex : 0);
        });

        star.addEventListener("click", function () {
            const value = index + 1;
            ratingInput.value = value;
            highlightStars(value);

            clickedStarIndex = value;
        });
    });
});

function highlightStars(value) {
    stars.forEach((star, i) => {
        if (i < value) {
            star.classList.add("active", "hovered");
        } else {
            star.classList.remove("active", "hovered");
        }
    });
}

function setRating(value) {
    console.log("setRating called with value:", value);
    const ratingInput = document.getElementById("rating");

    const finalValue = (value === clickedStarIndex) ? 0 : value;
    ratingInput.value = finalValue;
    highlightStars(finalValue);

    clickedStarIndex = finalValue;
}

 function confirmDelete() {
        var result = confirm("Are you sure you want to delete this review?");
        if (result) {
            document.forms[0].submit();
        } else {
            event.preventDefault();
        }
   }

   function confirmCancel() {
           var result = confirm("Are you sure you want cancel?");
           if (result) {
               document.forms[0].submit();
           } else {
                event.preventDefault();
           }
   }