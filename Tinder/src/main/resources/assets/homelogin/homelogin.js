$(document).ready(function (event) {

    $.ajax({
        url: "/api/matches",
        method: "GET",
        contentType: "application/json",
        success: function (response) {
            console.log(response)
            let matchE = document.getElementById("tab1");
            let html=``
            if(response && response.length) {
                response.forEach(data =>{

                    html+=`
<div  class=" showMatch col-4">
                       <a 
                       href="/message/${data.id}"
                       >
                           <div class="">
                               <img src="${data.photo[0].imageUrl}"/>
                               <span class="matchesName">${data.fullname}</span>

                           </div>
                       </a>
</div>`

                })
            }
            matchE.innerHTML = html;
        },
        error: function (error) {
            console.log("Đã có lỗi xảy ra khi like: " + error);
        }
    })
    var swipedProfiles = [];
    var currentProfile = null;

    var startX = 0;
    var startY = 0;
    $(".close1").on("click", function () {
        swipeDislike();
    });

    $(".btn-heart").on("click", function () {
        swipeLike();
    });

    document.addEventListener("keydown", function (event) {
        var key = event.key;
        if (key === "ArrowRight") {
            swipeLike();
        } else if (key === "ArrowLeft") {
            swipeDislike();
        }
    });

    addNewProfile();

    function swipe() {
        Draggable.create("#photo", {
            throwProps: false,
            onDragEnd: function (endX) {
                if (Math.round(this.endX) > 0) {
                    swipeLike();
                } else {
                    swipeDislike();
                }
                console.log(Math.round(this.endX));
            }
        });
    }

    function swipeLike() {
        if (currentProfile) {
            const idCurrentUser = document.getElementById("idcurrent").value;
            const idLikedProfile = currentProfile.id;
            const currentDate = new Date();
            $.ajax({
                url: "/api/likes/like",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    likerId: idCurrentUser,
                    likeeId: idLikedProfile,
                    likeDate: currentDate
                }),
                success: function (response) {
                    if (response) {
                        showMatchPopup();
                    }
                },
                error: function (error) {
                    console.log("Đã có lỗi xảy ra khi like: " + error);
                }
            })

            var $photo = $("div.content").find('#photo');
            var $likeText = $("div.content").find('.like-text');
            var swipe = new TimelineMax({
                repeat: 0,
                yoyo: false,
                repeatDelay: 0,
                onComplete: remove,
                onCompleteParams: [$photo]
            });
            swipe.staggerTo($photo, 0.8, {
                bezier: [{left: "+=400", top: "+=300", rotation: "60"}],
                ease: Power1.easeInOut,
                onComplete: function () {
                    $likeText.css('opacity', 1);
                }
            });
            swipedProfiles.push(currentProfile.id);
            addNewProfile();
        }
    }

    function swipeDislike() {
        if (currentProfile) {
            var $photo = $("div.content").find('#photo');
            var $dislikeText = $("div.content").find('.dislike-text');
            var swipe = new TimelineMax({
                repeat: 0,
                yoyo: false,
                repeatDelay: 0,
                onComplete: remove,
                onCompleteParams: [$photo]
            });
            swipe.staggerTo($photo, 0.8, {
                bezier: [{left: "-=350", top: "+=300", rotation: "-60"}],
                ease: Power1.easeInOut,
                onComplete: function () {
                    $dislikeText.css('opacity', 1);
                }
            });
            swipedProfiles.push(currentProfile.id);
            addNewProfile();
        }
    }

    function remove(photo) {
        $(photo).remove();
    }

    function addNewProfile() {
        $.ajax({
            url: "api/userprofiles",
            method: "GET",
            success: function (datas) {
                console.log("helooo")
                let unswipedDatas = datas.filter(data => !swipedProfiles.includes(data.id));

                if (unswipedDatas.length === 0) {
                    alert("Bạn đã gặp hết tất cả người dùng.");
                    return;
                }
                let index = Math.floor(Math.random() * unswipedDatas.length);
                currentProfile = unswipedDatas[index];
                console.log(currentProfile)

                $("div.content").find('.like-text').css('opacity', 0);
                $("div.content").find('.dislike-text').css('opacity', 0);
                // $("div.content").find('.info').find('h3').eq(0).text(currentProfile.fullName);
                // $("div.content").find('.info').find('h3').eq(1).text(currentProfile.age);


                $("div.content").prepend(`<div class="photo" id="photo" style="background-image:url(${currentProfile.photos[0].imageUrl});"></div>`);

                $("#fullName").text(currentProfile.fullName);
                $("#age").text(currentProfile.age);
                $("#namefull").text(currentProfile.fullName)
                swipe();


                // data = datas[index];
                // var fullName = data.fullName;
                // var age = data.age;
                // var photo = data.photos;
                // console.log(data)
                // console.log(photo)
                // // photo.forEach(pt =>{
                //     $("div.content").prepend(`<div class="photo" id="photo" style="background-image:url(${photo[0].imageUrl});"></div>`);
                // // })
                //
                //
                // $("div.content").find('.like-text').css('opacity', 0);
                // $("div.content").find('.dislike-text').css('opacity', 0);
                // $("div.content").find('.info').find('h3').eq(0).text(fullName);
                // $("div.content").find('.info').find('h3').eq(1).text(age);
                //

                //
                // swipedProfiles.push(data.id);
                // swipe();
            }
        })
        swipe();
    }
});

function showMatchPopup() {
    swal.fire({
        title: 'Tương hợp!',
        text: 'Bạn và người dùng này đã tương hợp!',
        icon: 'success',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'OK'
    })
}