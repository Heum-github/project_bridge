const recbtn = document.querySelector("#recommender");

recbtn.addEventListener("click", () => {
	const user_id = document.querySelector('#user_id_real').value;
	$.ajax({
		type: 'GET',
		url: "matchingPage/recommender",
		data: {
			user_id: user_id
		},
		success: function(response) {
			const recommended = $("#recommended");
			recommended.html("");
			for (let i = 0; i < response.length; i++) {
				let list = response[i];
				let content = `
					<div style="flex: 1; text-align : center;">
						<audio controls>
	    	    	        <source src="data:audio/mp3;base64,${list.song_file}" type="audio/mpeg">
	        	        </audio> <br>
	                    <span>${list.user_song_name}</span> 
	                    <span> - </span>
	                    <span>${list.user_singer}</span><br>
						<span>${list.user_id}</span>
					</div>
			`;
				recommended.append(content);
			}
			const mainline = $("#mainline");
			mainline.html("추천 받은 음원을 들어보고 마음에 드는 사람을 검색하여 친구가 되어보세요!");

		},
		error: function (error) {
			console.error('송신 실패', error);
		}
	});
})
