const searchProfileBtn = $("#searchProfileBtn");

searchProfileBtn.on("click", () => {
	const searchProfileValue = $("#searchProfileValue").val();
	console.log(searchProfileValue)
	$.ajax({
		type: "GET",
		url: "matchingPage/searchProfile",
		data: { user: searchProfileValue },
		success: function(res) {
			console.log(res);
			const profileList = $("#profileList");
			if (res[0] != null) {
				profileList.html("");
				for (let i = 0; i < res.length; i++) {
					let list = res[i];
					console.log(`${list.user_id}`);
					let content = `
				    <div id="profile" class="col-md-6 col-lg-3">
				        <div class="staff">
				            <div class="text pt-3 px-3 pb-4 text-center">
				                <input id="user_id" name="user_id" type="hidden" value="${list.user_id}"> 
								<input id="user_nick" name="user_nick" type="hidden" value="${list.user_nick}">
				                <input id="user_birthday" name="user_birthday" type="hidden" value="${list.user_birthday}"> 
								<input id="user_fav" name="user_fav" type="hidden" value="${list.user_fav}">
				                <input id="user_intro" name="user_intro" type="hidden" value="${list.user_intro}">
				                <h3>${list.user_nick}</h3>
				                <span>${list.user_birthday}</span>
								<div class="faded">
									<p>${list.user_fav}</p>
								</div>
				                <button id="profileBtn" class="btn btn-primary">프로필카드 보기</button>
				            </div>
				        </div>
				    </div>
					`
					profileList.append(content);
				};
			} else {
				console.log("결과없음")
				profileList.html("");
				let content = "<p>조건에 맞는 사용자를 찾을 수 없습니다</p>"
				profileList.append(content);
			};
		},
		error: function(error) {
			console.log(error)
		}
	})
})




