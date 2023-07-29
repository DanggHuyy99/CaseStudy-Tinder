var stompClient = null;
var isHidden = false
let likeeIdUser = 0;
$(document).ready(function () {
    let superLikedUsers = [];

    function superLikeCheckFunction() {
        return superLikedUsers.includes(likeeIdUser);
    }

    function superLikeSaveFunction() {
        if (!superLikedUsers.includes(likeeIdUser)) {
            superLikedUsers.push(likeeIdUser);
        }
    }

    function getRandomColor() {
        const letters = '0123456789ABCDEF';
        let color = '#';
        for (let i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    }

    document.getElementById("superLike").addEventListener("click", function () {
        const idCurrentUser = document.getElementById("idcurrent").value;
        isVipAccountToSuperLike(idCurrentUser);
    });

    function isVipAccountToSuperLike(userId) {
        $.ajax({
            url: "/api/users/isVipAccount",
            method: "GET",
            data: {
                userId: userId,
            },
            success: function (isVip) {
                handleSuperLike(isVip);
            },
            error: function (error) {
                console.log("Lỗi khi kiểm tra tài khoản VIP: " + error);
            },
        });
    }

    function handleSuperLike(isVip) {
        const randomColor = getRandomColor();
        const idCurrentUser = document.getElementById("idcurrent").value;

        if (superLikeCheckFunction()) {
            swal.fire({
                icon: 'none',
                title: '<i class="fas fa-heart" style="color: #ff0000;"></i>',
                html: 'Bạn đã like người dùng này!',
                position: 'top-end',
                showConfirmButton: false,
                timer: 4000,
                width: '300px',
                padding: '10px',
                customClass: {
                    popup: 'animate__animated animate__bounceInRight'
                }
            });
            return; // Ngăn không cho gửi Super Like lần nữa
        }

        if (isVip) {
            $.ajax({
                url: "/api/superlikes/superLike",
                method: "POST",
                data: {
                    likerId: idCurrentUser,
                    likeeId: likeeIdUser,
                },
                success: function (response) {
                    console.log("Super Like!");
                },
                error: function (error) {
                    console.log("Đã có lỗi xảy ra khi Super Like: " + error);
                    // Xử lý khi Super Like gặp lỗi (nếu cần)
                }
            });
            superLikeSaveFunction(likeeIdUser)
            swal.fire({
                icon: 'none', // Xóa icon mặc định
                title: '<i class="fas fa-thumbs-up" style="color: ' + randomColor + '"></i>', // Sử dụng icon "like" từ Font Awesome
                html: 'Đã gửi thông báo Super Like đến người dùng!', // Nội dung thông báo
                position: 'top-end',
                showConfirmButton: false,
                timer: 4000,
                width: '300px', // Thiết lập chiều rộng
                padding: '10px', // Thiết lập khoảng cách giữa nội dung và viền
                customClass: {
                    popup: 'animate__animated animate__backInLeft' // Hiệu ứng slide-in từ góc phải bên trên
                }
            });
        } else {
            swal.fire({
                icon: 'error',
                title: 'Chỉ VIP mới được sử dụng Super Like',
                text: 'Bạn cần nâng cấp tài khoản lên VIP để sử dụng Super Like.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: 'Nâng cấp tài khoản',
                showCancelButton: true,
                cancelButtonText: 'Để sau',
                cancelButtonColor: '#d33'
            }).then((result) => {
                if (result.isConfirmed) {
                    $.ajax({
                        url: "/api/users/upgradeAccount",
                        method: "POST",
                        data: {
                            userId: idCurrentUser,
                        },
                        success: function (response) {
                            swal.fire({
                                icon: "success",
                                title: "Nâng cấp tài khoản thành công!",
                                text: "Tài khoản của bạn đã được nâng cấp thành VIP.",
                            });
                        },
                        error: function (error) {
                            swal.fire({
                                icon: "error",
                                title: "Lỗi",
                                text: "Đã có lỗi xảy ra khi nâng cấp tài khoản. Vui lòng thử lại sau.",
                            });
                        },
                    });
                } else {
                    // Xử lý khi người dùng nhấn nút "Để sau"
                    console.log("Người dùng đã nhấn nút để sau");
                }
            });
        }
    }







    var grabButton = document.getElementById("grab");
    var tinderNotification = document.querySelector(".tinder-notification");
    var isDragging = false;
    var initialY;
    var currentY;
    var minY = 542;
    var maxY = 700;
    var originalTop = 542;


// Xử lý sự kiện khi chuột được nhấn xuống
    grabButton.addEventListener("mousedown", function (e) {
        isDragging = true;
        initialY = e.clientY;
        currentY = initialY;
        grabButton.style.cursor = "grabbing";
    });

// Xử lý sự kiện khi chuột được di chuyển
    document.addEventListener("mousemove", function (e) {
        if (isDragging) {
            var offsetY = e.clientY - currentY;
            var newY = tinderNotification.offsetTop + offsetY;

            // Giới hạn di chuyển trong tầm từ top 500px đến top 650px
            newY = Math.max(minY, Math.min(newY, maxY));

            tinderNotification.style.top = newY + "px";
            currentY = e.clientY;
        }
    });

// Xử lý sự kiện khi chuột được thả ra
    document.addEventListener("mouseup", function () {
        isDragging = false;
        grabButton.style.cursor = "grab";

        if (parseInt(tinderNotification.style.top) > 680 && !isHidden) {
            tinderNotification.classList.add("fade-out");
            isHidden = true; // Đánh dấu đoạn div đã ẩn đi
        }
    });

    connect();
});

function closeChat() {
    isChatOpen = false;
    document.getElementById("messageInput").removeEventListener("keyup", handleEnterKey);
    document.getElementById("chatContainer").style.display = "none";
    document.getElementById("message-container").style.display = "none";
    document.getElementById("default-container").style.display = "flex";
    // stompClient.disconnect();
}

let receiverId = 0;

async function getUrlPhotoByIdUser(idUser) {
    try {
        const resp = await $.ajax({
            url: "/api/users/getPhotoById",
            method: "GET",
            contentType: "application/json",
            data: {
                id: idUser
            }
        });
        const url = resp[0].imageUrl;
        console.log(url);
        return url;
    } catch (error) {
        console.log("Lỗi:", error);
        return null;
    }
}

async function handleMessage(messageDto) {
    if (messageDto.content !== null && messageDto.receiverId === +document.getElementById('idcurrent').value) {
        const currentUserId = document.getElementById("idcurrent").value;
        const imgUrl = await getUrlPhotoByIdUser(messageDto.senderId);
        if (imgUrl !== null) {
            showNotification(messageDto.senderName, messageDto.senderId, imgUrl);
            getChatHistory(currentUserId, receiverId, function (chatHistory) {
                displayChatHistory(chatHistory);
            });
            console.log("Nhận tin nhắn mới: ", messageDto);
        }
    }
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Web Socket Opened...');
        stompClient.subscribe('/topic/public', function (message) {

            var messageDto = JSON.parse(message.body);

            handleMessage(messageDto);

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
            console.log(chatHistory)
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
    var chatMessages = document.getElementById("chatMessages");
    chatMessages.innerHTML = ""; // Xóa nội dung cũ trước khi hiển thị lịch sử chat mới

    // Kiểm tra xem chatHistory có dữ liệu không
    if (chatHistory && chatHistory.length > 0) {
        chatHistory.forEach(function (message) {
            var sender = message.sender.username;
            var content = message.content;
            var receiver = message.receiver.username;
            var sendDate = formatTimestamp(message.sentAt);
            var idCurrent = message.sender.id;
            if (+idCurrent === +document.getElementById("idcurrent").value) {
                console.log("hello")
            }

            // var messageDiv = document.createElement("div");
            // messageDiv.innerText = sender + ": " + content;
            //
            // chatMessages.appendChild(messageDiv);
            var messageDivParent = document.createElement("div");
            messageDivParent.classList.add("parent")
            console.log()
            if (previousSender !== sender && +idCurrent !== +document.getElementById("idcurrent").value) {
                var avatarImg = document.createElement("img");
                avatarImg.src = imageUrlGlobal;
                avatarImg.alt = "Avatar";
                avatarImg.classList.add("avatar");

                messageDivParent.appendChild(avatarImg);

            }
            previousSender = sender;
            var messageDiv = document.createElement("span");
            messageDiv.innerText = content;

            var dateDivSender = document.createElement("span")
            dateDivSender.classList.add("date-sent-sender")
            dateDivSender.innerHTML = sendDate;

            var dateDivReceiver = document.createElement("span")
            dateDivReceiver.classList.add("date-sent-receiver")
            dateDivReceiver.innerHTML = sendDate;

            const usernameCurrent = document.getElementById("username-current").value;

            if (sender === usernameCurrent) {
                messageDiv.classList.add("sender-message");
                messageDivParent.appendChild(dateDivSender)
                messageDivParent.appendChild(messageDiv);
                messageDivParent.classList.add("parent-right")
            } else {
                messageDiv.classList.add("receiver-message");
                messageDivParent.appendChild(messageDiv);
                messageDivParent.appendChild(dateDivReceiver)
                messageDivParent.classList.add("parent-left")

            }

            chatMessages.appendChild(messageDivParent);
        });
    } else {
        var noChatDiv = document.createElement("div");
        noChatDiv.innerText = "Không có lịch sử chat.";
        chatMessages.appendChild(noChatDiv);
    }
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

function formatTimestamp(timestamp) {
    const dateObj = new Date(timestamp);

    const hours = String(dateObj.getHours()).padStart(2, "0");
    const minutes = String(dateObj.getMinutes()).padStart(2, "0");
    const day = String(dateObj.getDate()).padStart(2, "0");
    const month = String(dateObj.getMonth() + 1); // Lưu ý: Tháng trong JavaScript bắt đầu từ 0, vì vậy cần +1 để có tháng chính xác.
    const year = dateObj.getFullYear();

    return `${hours}:${minutes} ${day}/${month}/${year}`;
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
    if (content.trim() === "") return;
    var message = {
        receiverId: receiveID,
        content: content,
        senderId: document.getElementById("idcurrent").value
    };
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
let isChatOpen = false;

let urlImageSendMessage = '';
let userIdSendMessage = 0;
let fullNameSendMessage = '';

function openChat(userId, username, urlImage) {
    urlImageSendMessage = document.getElementById("url-img-current").value;
    userIdSendMessage = document.getElementById("idcurrent").value;
    fullNameSendMessage = document.getElementById("full-name-current").value;

    console.log(urlImageSendMessage)
    console.log(userIdSendMessage)
    console.log(fullNameSendMessage)
    receiverId = userId;

    imageUrlGlobal = urlImage;
    console.log(imageUrlGlobal)
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
    getChatHistory(currentUserId, userId, function (chatHistory) {
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

    document.getElementById("messageInput").addEventListener("keyup", handleEnterKey);
    isChatOpen = true;

}

const handleSubmitMessage = async () => {
    console.log("userId", userIdGlobal)
    var messageInput = document.getElementById("messageInput");
    var content = messageInput.value;
    await sendMessage(userIdGlobal, content);
    messageInput.value = "";

    var sendMessageBtn = document.getElementById("sendMessageBtn");
    sendMessageBtn.classList.remove("active");
}

function handleEnterKey(event) {
    if (isChatOpen && event.keyCode === 13) {
        event.preventDefault();
        sendMessage(userIdGlobal, event.target.value);
        event.target.value = "";
        var sendMessageBtn = document.getElementById("sendMessageBtn");
        sendMessageBtn.classList.remove("active");
    }

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
        document.getElementById("dislike").style.display = "block";
        document.getElementById("like").style.display = "none";
    });

    $(".btn-heart").on("click", function () {
        swipeLike();
        document.getElementById("like").style.display = "block";
        document.getElementById("dislike").style.display = "none";
    });

    document.addEventListener("keydown", function (event) {
        var key = event.key;
        if (key === "ArrowRight") {
            swipeLike();
            document.getElementById("like").style.display = "block";
            document.getElementById("dislike").style.display = "none";
        } else if (key === "ArrowLeft") {
            swipeDislike();
            document.getElementById("dislike").style.display = "block";
            document.getElementById("like").style.display = "none";
        }
    });


    addNewProfile();

    function swipe() {
        Draggable.create("#photo", {
            throwProps: false,
            onDrag: function () {
                if (Math.round(this.x) < 50 && Math.round(this.x) > -50) {
                    document.getElementById("like").style.display = "none";
                    document.getElementById("dislike").style.display = "none";
                }
                if (Math.round(this.x) > 50) {

                    document.getElementById("like").style.display = "block";
                    document.getElementById("dislike").style.display = "none";
                    if (Math.round(this.x) < 150) {
                        document.getElementById("like").style.opacity = "0.3";
                    } else if (Math.round(this.x) < 300) {
                        document.getElementById("like").style.opacity = "0.7";
                    } else {
                        document.getElementById("like").style.opacity = "1";
                    }
                } else if (Math.round(this.endX) < -50) {
                    console.log(this.pointerX)
                    console.log(this.pointerY)
                    // document.getElementById("photo").style.transform =  'rotate(-30deg)';
                    // document.getElementById("photo").style.transformOrigin= 'left bottom';
                    // document.getElementById("photo").style.transform = "rotate(" + Math.round(this.x) + "deg)";
                    // document.getElementById("photo").style.transformOrigin = (this.pointerX/10) + "px " + this.pointerY + "px";

                    // var centerX = this.pointerX - this.x; // Tính tọa độ X tương đối của trung tâm của #photo
                    // var centerY = this.pointerY - this.y * 10;
                    // console.log(centerX, "centex X")
                    // console.log(centerY, "centey Y")
                    //
                    // var scrollY = Math.round(this.endX);
                    // var angle = scrollY / 10 + "deg"; // Góc xoay tính theo vị trí kéo (tùy chỉnh)
                    //
                    // document.getElementById("photo").style.transform = "rotate(" + angle + ")";
                    // document.getElementById("photo").style.transformOrigin = centerX + 'px' + -centerY + 'px';

                    document.getElementById("dislike").style.display = "block";
                    document.getElementById("like").style.display = "none";
                    if (Math.round(this.x) > -150) {
                        document.getElementById("dislike").style.opacity = "0.3";
                    } else if (Math.round(this.x) > -300) {
                        document.getElementById("dislike").style.opacity = "0.7";
                    } else {
                        document.getElementById("dislike").style.opacity = "1";
                    }
                }
                console.log("Vị trí X: ", Math.round(this.x));
            },
            onDragEnd: function (endX) {
                if (Math.round(this.endX) > 50) {
                    console.log(this.endX)
                    document.getElementById("like").style.display = 'block'
                    document.getElementById("dislike").style.display = 'none'
                    swipeLike();
                } else if (Math.round(this.endX) < -50) {
                    document.getElementById("dislike").style.display = 'block'
                    document.getElementById("like").style.display = 'none'
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
                url: "api/users/isVipAccount",
                method: "GET",
                data: {
                    userId: idCurrentUser,
                },
                success: function (isVip) {
                    if (isVip) {
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
                        });

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
                    } else {
                        $.ajax({
                            url: "/api/likes/countLike",
                            method: "GET",
                            data: {
                                likerId: idCurrentUser,
                            },
                            success: function (likeCount) {
                                console.log("like count", likeCount)
                                if (likeCount < 5) {
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
                                } else {
                                    swal.fire({
                                        icon: 'error',
                                        title: 'Hết lượt thích',
                                        text: 'Bạn đã sử dụng hết số lượt thích cho hôm nay. Vui lòng nâng cấp lên tài khoản VIP để có lượt like vô hạn!',
                                        confirmButtonColor: '#3085d6',
                                        confirmButtonText: 'Nâng cấp tài khoản',
                                        showCancelButton: true,
                                        cancelButtonText: 'Để sau',
                                        cancelButtonColor: '#d33'
                                    }).then((result) => {
                                        if (result.isConfirmed) {
                                            swal.fire({
                                                icon: "warning",
                                                title: "Xác nhận nâng cấp tài khoản",
                                                text: "Bạn có muốn nâng cấp tài khoản thành VIP?",
                                                showCancelButton: true,
                                                confirmButtonText: "Nâng cấp tài khoản",
                                                cancelButtonText: "Hủy",
                                                reverseButtons: true,
                                            }).then((result) => {
                                                // Nếu người dùng bấm nút "Nâng cấp tài khoản"
                                                if (result.isConfirmed) {
                                                    // Gọi API để nâng cấp tài khoản
                                                    $.ajax({
                                                        url: "/api/users/upgradeAccount",
                                                        method: "POST",
                                                        data: {
                                                            userId: idCurrentUser,
                                                        },
                                                        success: function (response) {
                                                            swal.fire({
                                                                icon: "success",
                                                                title: "Nâng cấp tài khoản thành công!",
                                                                text: "Tài khoản của bạn đã được nâng cấp thành VIP.",
                                                            });
                                                        },
                                                        error: function (error) {
                                                            swal.fire({
                                                                icon: "error",
                                                                title: "Lỗi",
                                                                text: "Đã có lỗi xảy ra khi nâng cấp tài khoản. Vui lòng thử lại sau.",
                                                            });
                                                        },
                                                    });
                                                }
                                            });
                                        } else {
                                            // Xử lý khi người dùng nhấn nút "Để sau"
                                            console.log("Người dùng đã nhấn nút để sau");
                                        }
                                    });
                                }
                            },
                            error: function (error) {
                                console.log("Lỗi khi lấy số lượt thích: " + error);
                            }
                        })
                    }
                }
            })


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

    function hasLikedOrMatched(likerId, profileId) {
        return new Promise(function(resolve, reject) {
            $.ajax({
                url: "/api/hasLikedOrMatched",
                method: "GET",
                data: {
                    likerId: likerId,
                    profileId: profileId
                },
                success: function(response) {
                    resolve(response);
                },
                error: function(error) {
                    reject(error);
                }
            });
        });
    }


    function addNewProfile() {
        $.ajax({
            url: "api/userprofiles",
            method: "GET",
            success: function (datas) {
                console.log("helooo")
                let unswipedDatas = datas.filter(data => !swipedProfiles.includes(data.id));

                if (unswipedDatas.length === 0) {
                    showUserOver()
                    return;
                }
                let index = Math.floor(Math.random() * unswipedDatas.length);
                currentProfile = unswipedDatas[index];
                console.log(currentProfile)

                likeeIdUser = currentProfile.id;

                $("div.content").find('.like-text').css('opacity', 0);
                $("div.content").find('.dislike-text').css('opacity', 0);
                // $("div.content").find('.info').find('h3').eq(0).text(currentProfile.fullName);
                // $("div.content").find('.info').find('h3').eq(1).text(currentProfile.age);


                $("div.content").prepend(`
                    <div class="photo" id="photo" style="background-image:url(${currentProfile.photos[0].imageUrl});">
                    <div id="dislike">KHÔNG</div>
                     <div id="like">THÍCH</div>


                       <div class="info info-profile">
                            <h3 id="fullName">${currentProfile.fullName}</h3>
                            <!--                        <h1 id="namefull"></h1>-->
                            <h3 id="age">${currentProfile.age}</h3>

                       </div>
                    </div>

                    `);
                swipe();
            }
        })
        swipe();
    }


    // function addNewProfile() {
    //     $.ajax({
    //         url: "api/userprofiles",
    //         method: "GET",
    //         success: function (datas) {
    //             console.log("helooo")
    //             let unswipedDatas = datas.filter(data => !swipedProfiles.includes(data.id));
    //
    //             if (unswipedDatas.length === 0) {
    //                alert("hello")
    //                 return;
    //             }
    //
    //             for (const currentProfile of unswipedDatas) {
    //                 const profileId = currentProfile.id;
    //                 const likerId = document.getElementById("idcurrent").value;
    //
    //                 hasLikedOrMatched(likerId, profileId)
    //                     .then(function(response) {
    //                         if (!response) {
    //                             $("div.content").find('.like-text').css('opacity', 0);
    //                             $("div.content").find('.dislike-text').css('opacity', 0);
    //
    //                             $("div.content").prepend(`
    //                             <div class="photo" id="photo" style="background-image:url(${currentProfile.photos[0].imageUrl});">
    //                                 <div id="dislike">KHÔNG</div>
    //                                 <div id="like">THÍCH</div>
    //                                 <div class="info info-profile">
    //                                     <h3 id="fullName">${currentProfile.fullName}</h3>
    //                                     <h3 id="age">${currentProfile.age}</h3>
    //                                 </div>
    //                             </div>
    //                         `);
    //
    //
    //                             swipe();
    //                         }
    //                     })
    //                     .catch(function(error) {
    //                         console.log("Đã có lỗi xảy ra khi kiểm tra like/match: " + error);
    //                     });
    //             }
    //         }
    //     });
    //     swipe();
    // }

});



function showTinderNotification() {
    var notification = document.querySelector(".tinder-notification");
    notification.style.bottom = "0px"; // Hiển thị thông báo lên dưới màn hình
    setTimeout(function () {
        notification.style.bottom = "-100%"; // Ẩn thông báo xuống bên dưới màn hình sau 3 giây
    }, 99000);
    if (isHidden) {
        // Nếu đoạn div đã ẩn đi, xóa class fade-out để hiển thị lại đoạn div
        notification.classList.remove("fade-out");
        notification.style.top = originalTop + "px";
        isHidden = false; // Đánh dấu đoạn div đã hiển thị lại
    }
}

function showUserOver() {
    Swal.fire({
        title: "Thông báo!",
        text: "Bạn đã gặp hết tất cả người dùng.\nVui lòng mở rộng độ tuổi để tìm thêm tương hợp.",
        icon: "info",
        confirmButtonText: "OK",
        // timer: 7000,
        toast: true,
        position: "center-end",
        showClass: {
            popup: "animate__animated animate__fadeInRight"
        },
        hideClass: {
            popup: "animate__animated animate__fadeOutRight"
        }
    });
    document.getElementById("content1").style.display = 'none';
    showTinderNotification();
}

function showMatchPopup() {
    swal.fire({
        title: 'Tương hợp!',
        text: 'Bạn và người dùng này đã tương hợp!',
        icon: 'success',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'OK'
    })
}

function showNotification(username, userId, imgUrl) {
    var tinderNotification = document.querySelector(".tinder-notification");
    swal.fire({
        title: '<i class="fa-regular fa-envelope"></i> Tin nhắn mới',
        text: `Tin nhắn mới từ ${username} `,
        icon: 'envelope',
        timer: 5000,
        toast: true,
        position: 'top-end',
        showCancelButton: true,
        confirmButtonText: '<i class="fas fa-eye"></i> Xem tin nhắn',
        cancelButtonText: '<i class="fas fa-times"></i> Đóng',
        showClass: {
            popup: 'animate__animated animate__lightSpeedInRight'
        },
        hideClass: {
            popup: 'animate__animated animate__lightSpeedOutRight'
        }
    }).then((result) => {
        if (result.isConfirmed) {

            openChat(userId, username, imgUrl)
        }
    });
}





