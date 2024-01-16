const divs = document.querySelectorAll('#profile');
const detail = document.querySelector(".profilemodal");

divs.forEach((div) => {
   div.addEventListener('click', function() {
      const user_id = div.querySelector('#user_id').value;
      console.log(user_id);
      $.ajax({
         type: 'GET',
         url: 'matchingPage/matchingProfile',
         data: { "user_id": user_id },
         success: function(response) {
            const user_nick = div.querySelector('#user_nick').value;
            const user_birthday = div.querySelector('#user_birthday').value;
            const user_fav = div.querySelector('#user_fav').value;
            const user_intro = div.querySelector('#user_intro').value;
            
            const today = new Date();
            const birthDate = new Date(user_birthday);
            let age = today.getFullYear() - birthDate.getFullYear();
            let m = today.getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }
            console.log(response);
            detail.classList.remove('hidden');
            detail.innerHTML = `
                    <div class="container">
                     <div class="col-md-6">
                        <div class="profile cdRotate"  style="margin-bottom: 40px">
							<div class = "cdCenter"></div>
						</div>
                             <div class="profile-container">
                              <div class="user_nick profile-title">닉네임</div>
                              <div class="profile-content">${user_nick}</div>
                           </div>
                           <div class="profile-container">
                              <div class="song_genre profile-title">나이</div>
                              <div class="profile-content">${age}</div>
                           </div>
                           <div class="profile-container">
                              <div class="user_singer profile-title">좋아하는 가수</div>
                              <div class="profile-content">${user_fav}</div>
                           </div>
                           <div class="profile-container">
                              <div class="user_text profile-title">소개</div>
                              <div class="profile-content">${user_intro}</div>
                         </div>
                         <a href="mychat"><button class="btn btn-primary">채팅하기</button></a>
                     <button class="btn btn-primary" id = "closemodal">닫기</button>
                     </div>
                     <div class="col-md-6" style = "border : 1px solid white; border-radius : 30px;">
                        <h3 class="profile-list-title">업로드한 노래</h3>
                           <ul class="profile-list profilesong" style="padding: 0;"></ul>
                        </div>
                    </div>
                `;

            const song = document.querySelector(".profilesong");
            for (let i = 0; i < response.length; i++) {
               let list = response[i];
               let content = `
                     <audio controls>
                         <source src="data:audio/mp3;base64,${list.song_file}" type="audio/mpeg">
                     </audio>
                     <span class="profile-list-item">${list.user_song_name}</span>
                <span> - </span>
                     <span class="profile-list-item">${list.user_singer}</span>
               <br><br>

                    `;
               song.innerHTML += content; // innerHTML로 수정
            }
            const searchProfileBtn = $("#searchProfileBtn");

         },
         error: function(error) {
            console.error(error);
         }
      });
   });
});

document.addEventListener("click", (e) => {
   if (e.target.id === "closemodal") {
      detail.classList.add('hidden');
      detail.innerHTML = "";
   }
});
