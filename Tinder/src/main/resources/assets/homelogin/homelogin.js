$(document).ready(function(event) {
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
            throwProps:true,
                onDragEnd:function(endX) {
                if(Math.round(this.endX) > 0 ) {
                    swipeLike();
                }
                else {
                    swipeDislike();
                }
                console.log(Math.round(this.endX));
            }
        });
    }
    function swipeLike() {
        var $photo = $("div.content").find('#photo');
        var $likeText = $("div.content").find('.like-text');
        var swipe = new TimelineMax({ repeat: 0, yoyo: false, repeatDelay: 0, onComplete: remove, onCompleteParams: [$photo] });
        swipe.staggerTo($photo, 0.8, { bezier: [{ left: "+=400", top: "+=300", rotation: "60" }], ease: Power1.easeInOut, onComplete: function () { $likeText.css('opacity', 1); } });
        addNewProfile();
    }
        function swipeDislike() {
            var $photo = $("div.content").find('#photo');
            var $dislikeText = $("div.content").find('.dislike-text');
            var swipe = new TimelineMax({ repeat: 0, yoyo: false, repeatDelay: 0, onComplete: remove, onCompleteParams: [$photo] });
            swipe.staggerTo($photo, 0.8, { bezier: [{ left: "-=350", top: "+=300", rotation: "-60" }], ease: Power1.easeInOut, onComplete: function () { $dislikeText.css('opacity', 1); } });
            addNewProfile();
        }
            function remove(photo) {
                $(photo).remove();
            }
            function addNewProfile() {
                var names = ['Lieke', 'Christina', 'Sanne', 'Soraya', 'Chanella', 'Larissa', 'Michelle'][Math.floor(Math.random() * 7)];
                var ages = ['19','22','18','27','21', '18', '24'][Math.floor(Math.random() * 7)]
                var photos = ['1', '2', '3', '4'][Math.floor(Math.random() * 4)]
                $("div.content").prepend('<div class="photo" id="photo" style="background-image:url(/assets/imgs/'+photos+'.jpg)">'
            );
                swipe();
            }
        });

