var stompClient = null;

$(document).ready(function () {
    connect();
});

function closeChat() {
    document.getElementById("chatContainer").style.display = "none";
    document.getElementById("message-container").style.display = "none";
    document.getElementById("default-container").style.display = "flex";
    stompClient.disconnect();
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Web Socket Opened...');
        stompClient.subscribe('/topic/public', function (message) {
            var messageDto = JSON.parse(message.body);

            if (message.content !== null && messageDto.receiverId === +document.getElementById('idcurrent').value) {
                console.log("Nhận tin nhắn mới: ", messageDto);
                //ve them tin nhan.
            }

        });
    });
}


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}


function showChat(receiverUsername) {
    // Hiển thị khung chat với người dùng có username là receiverUsername
    var chatContainer = document.getElementById("chatContainer");
    chatContainer.style.display = "block";
    document.getElementById("chatUsername").innerText = "Chat with " + receiverUsername;
}


// Khi click vào một tương hợp
function onMatchClick(receiverUsername) {
    // Hiển thị khung chat và gửi tin nhắn đến tương hợp đó
    showChat(receiverUsername);
    // Gửi tin nhắn chào hỏi đến tương hợp
    sendMessage(receiverUsername, "Xin chào, chúng ta đã trùng khớp. Hãy cùng nhắn tin nhé!");
}

// Khi nhấn nút gửi trong khung chat
function onSendMessageClick() {
    var receiverId = document.getElementById("receiverId").value;
    var content = document.getElementById("messageInput").value;
    if (content.trim() !== "") {
        sendMessage(receiverId, content);
        var chatMessages = document.getElementById("chatMessages");
        var listItem = document.createElement("li");
        listItem.innerText = "You: " + content;
        chatMessages.appendChild(listItem);
        document.getElementById("messageInput").value = "";
    }
}

function getChatHistory(senderId, receiverId, callback) {
    var messageRequest = {
        senderId: senderId,
        receiverId: receiverId
    };
    $.ajax({
        url: "/api/chat/getChatHistory",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(messageRequest),
        success: function (chatHistory) {
            callback(chatHistory);
        },
        error: function () {
            console.log("Lỗi khi lấy lịch sử tin nhắn");
        }
    });
}

// function getChatHistory(receiverId, callback) {
//     //viet api
//     stompClient.send("/app/chat.getChatHistory", {}, JSON.stringify({receiverId: receiverId}));
//     stompClient.subscribe('/user/queue/chatHistory', function (message) {
//         console.log("Received message from server:", message);
//         var chatHistory = JSON.parse(message.body);
//         callback(chatHistory);
//     });
// }

let previousSender = null;

function displayChatHistory(chatHistory) {
    console.log(chatHistory)
    var chatMessages = document.getElementById("chatMessages");
    chatMessages.innerHTML = ""; // Xóa nội dung cũ trước khi hiển thị lịch sử chat mới

    // Kiểm tra xem chatHistory có dữ liệu không
    if (chatHistory && chatHistory.length > 0) {
        chatHistory.forEach(function (message) {
            var sender = message.sender.username;
            var content = message.content;
            var receiver = message.receiver.username;

            // var messageDiv = document.createElement("div");
            // messageDiv.innerText = sender + ": " + content;
            //
            // chatMessages.appendChild(messageDiv);
            var messageDivParent = document.createElement("div");
            messageDivParent.classList.add("parent")

            if (previousSender !== sender) {
                var avatarImg = document.createElement("img");
                avatarImg.src = imageUrlGlobal;
                avatarImg.alt = "Avatar";
                avatarImg.classList.add("avatar");

                messageDivParent.appendChild(avatarImg);
                previousSender = sender;
            }

            var messageDiv = document.createElement("span");
            messageDiv.innerText = content;

            const usernameCurrent = document.getElementById("username-current").value;

            if (sender === usernameCurrent) {
                messageDiv.classList.add("sender-message");
            } else {
                messageDiv.classList.add("receiver-message");
            }
            messageDivParent.appendChild(messageDiv);
            chatMessages.appendChild(messageDivParent);
        });
    } else {
        var noChatDiv = document.createElement("div");
        noChatDiv.innerText = "Không có lịch sử chat.";
        chatMessages.appendChild(noChatDiv);
    }
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

function displayMessage(senderId, content) {
    // const chatMessages = document.getElementById("chatMessages");
    // const messageDiv = document.createElement("div");
    // messageDiv.innerText = sender + ": " + content;
    // chatMessages.appendChild(messageDiv);
    $.ajax({
        url: "/api/user/" + senderId,
        method: "GET",
        success: function (user) {
            var senderName = user.username; // Tên người gửi
            var chatMessages = document.getElementById("chatMessages");
            var listItem = document.createElement("li");
            listItem.innerText = senderName + ": " + content;
            chatMessages.appendChild(listItem);
            // Cuộn xuống dưới cùng của khung chat để hiển thị tin nhắn mới nhất
            chatMessages.scrollTop = chatMessages.scrollHeight;
        },
        error: function () {
            console.log("Lỗi khi lấy thông tin người gửi");
        }
    });
}
function clearCache() {
    if (window.performance && window.performance.clearResourceTimings) {
        console.log("clear resort")
        window.performance.clearResourceTimings();
    }
}

function sendMessage(receiveID, content) {
    console.log("receiveID", receiveID)
    if (content.trim() === "") return;
    var message = {
        receiverId: receiveID,
        content: content,
        senderId: document.getElementById("idcurrent").value
    };
    console.log(message)
    stompClient.send("/app/chat.addUser", {}, JSON.stringify(message));
    var chatMessages = document.getElementById("chatMessages");
    var messageDivParent = document.createElement("div");
    messageDivParent.classList.add("parent")

    var listItem = document.createElement("span");

    listItem.innerText = content;
    listItem.classList.add("sender-message"); // Thêm lớp CSS "sender-message"
    messageDivParent.appendChild(listItem);
    chatMessages.appendChild(messageDivParent);
    // Cuộn xuống dưới cùng của khung chat để hiển thị tin nhắn mới nhất
    chatMessages.scrollTop = chatMessages.scrollHeight;
}


let userIdGlobal = 0;
let imageUrlGlobal = "";
function openChat(userId, username, urlImage) {
    imageUrlGlobal = urlImage;
    userIdGlobal = userId;
    // window.currentReceiverId = userId;
    clearCache()
    disconnect()
    connect()


    document.getElementById("default-container").style.display = "none";
    document.getElementById("message-container").style.display = "flex";
    // document.getElementById("chatUsername").innerText = "Chat with " + username;

    var chatUsername = document.getElementById("chatUsername");
    chatUsername.innerHTML = `<img src="${imageUrlGlobal}" class="avatar-user" alt="User Image"> Chat with ${username}`;

    document.getElementById("chatMessages").innerHTML = "";

    document.getElementById("chatContainer").style.display = "flex";

    var currentUserId = document.getElementById("idcurrent").value;
    getChatHistory(currentUserId, userId, function(chatHistory) {
        displayChatHistory(chatHistory);
    });


    // getChatHistory(userId, function(chatHistory) {
    //     displayChatHistory(chatHistory);
    // });

    // Subscribe để nhận tin nhắn từ server

    // stompClient.subscribe('/user/queue/messages', function (message) {
    //     var messageDto = JSON.parse(message.body);
    //     displayMessage(messageDto.sender, messageDto.content);
    // });


    // Gán sự kiện click cho nút "Send Message"
    // document.getElementById("sendMessageBtn").addEventListener("click", async () => {
    //     console.log("userId", userId)
    //     var messageInput = document.getElementById("messageInput");
    //     var content = messageInput.value;
    //     await sendMessage(userId, content);
    //     messageInput.value = "";
    //
    // });

    // Gán sự kiện click cho nút "Close Chat"
    document.getElementById("closeChatBtn").addEventListener("click", closeChat);

}

const handleSubmitMessage = async () => {
    console.log("userId", userIdGlobal)
    var messageInput = document.getElementById("messageInput");
    var content = messageInput.value;
    await sendMessage(userIdGlobal, content);
    messageInput.value = "";
}


$(document).ready(function (event) {
    function showMatchUser() {
        $.ajax({
            url: "/api/matches",
            method: "GET",
            contentType: "application/json",
            success: function (response) {
                console.log(response)
                let matchE = document.getElementById("tab1");
                let html = ``
                if (response && response.length) {
                    response.forEach(data => {
                        html += `
                                <div  class=" showMatch col-4" onclick="openChat(${data.id}, '${data.fullname}', '${data.photo[0].imageUrl}')">
                                       <a 
                                       href="#"
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
    }


    function displayMessage(sender, content) {
        const chatMessages = document.getElementById("chatMessages");
        const messageDiv = document.createElement("div");
        messageDiv.innerText = sender + ": " + content;
        chatMessages.appendChild(messageDiv);
    }


    // document.getElementById("sendMessageBtn").addEventListener("click", function () {
    //     const messageInput = document.getElementById("messageInput");
    //     const content = messageInput.value;
    //
    //     const urlParams = new URLSearchParams(window.location.search);
    //     const receiverId = urlParams.get('receiverId');
    //
    //
    //     // Gửi tin nhắn
    //     $.ajax({
    //         url: "/api/sendMessage",
    //         method: "POST",
    //         contentType: "application/json",
    //         data: JSON.stringify({
    //             receiverId: receiverId,
    //             content: content
    //         }),
    //         success: function () {
    //             // Hiển thị tin nhắn đã gửi
    //             displayMessage("You", content);
    //             // Xóa nội dung trong ô input
    //             messageInput.value = "";
    //         },
    //         error: function (error) {
    //             console.log("Đã có lỗi xảy ra khi gửi tin nhắn: " + error);
    //         }
    //     });
    // });

    // document.getElementById("closeChatBtn").addEventListener("click", function () {
    //     // Ẩn khung chat khi người dùng click nút close
    //     document.getElementById("chatContainer").style.display = "none";
    // });

    showMatchUser()

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
            $.ajax({
                url: "/api/likes/like",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    likerId: idCurrentUser,
                    likeeId: idLikedProfile,
                }),
                success: function (response) {
                    if (response) {
                        showMatchPopup();
                        showMatchUser();
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

                $("#photo").off("click").on("click", function () {
                    // Hiển thị khung chat khi người dùng click vào profile
                    openChat(currentProfile.fullName);
                });

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

