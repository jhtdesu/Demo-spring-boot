$.get("/user", function(data) {
    $("#user").html(data.name);
    $(".unauthenticated").hide();
    $(".authenticated").show();
}).fail(function() {
    $(".unauthenticated").show();
    $(".authenticated").hide();
});

var logout = function () {
  $.post("/logout", function () {
    $("#user").html("");
    $(".unauthenticated").show();
    $(".authenticated").hide();
  });
  return true;
};

      // Example JavaScript to toggle authentication
      document.addEventListener("DOMContentLoaded", function () {
        const isAuthenticated = false; // Replace with actual authentication logic
        const unauthenticatedDiv = document.querySelector(".unauthenticated");
        const authenticatedDiv = document.querySelector(".authenticated");

        if (isAuthenticated) {
          unauthenticatedDiv.style.display = "none";
          authenticatedDiv.style.display = "block";
          document.getElementById("user").textContent = "User Name"; // Replace with actual user data
        } else {
          unauthenticatedDiv.style.display = "block";
          authenticatedDiv.style.display = "none";
        }
      });

      function logout() {
        // Add logout logic here
        alert("Logged out");
      }

      $(document).ready(function() {
        var logout = function () {
            var csrfToken = $("meta[name='_csrf']").attr("content");
            var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    
            $.ajax({
                type: "POST",
                url: "/logout",
                beforeSend: function (xhr) {
                    console.log("CSRF Header:", csrfHeader); // Add these for debugging
                    console.log("CSRF Token:", csrfToken);   //
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function () {
                    $("#user").html("");
                    $(".unauthenticated").show();
                    $(".authenticated").hide();
                },
                error: function(xhr, status, error) {
                    console.error("Logout failed:", status, error);
                }
            });
            return true;
        };
    
        // Attach the logout function to your button's onclick event (if you're doing it this way)
        // Example:
        // $("#logoutButton").on("click", logout);
    });