var currentPage = 1;

// 선택한 노래 DB에 저장함
function saveCheckboxState() {
	var song_nums = [];
	var ratings = [];

	document.querySelectorAll('input[name^="selectedSongs"]:checked').forEach(function(checkbox) {
		var song_num = checkbox.value;
		try {
			var rating = document.querySelector('input[name^="rating-' + song_num + '"][type="radio"]:checked');

			if (rating) {
				song_nums.push(parseInt(song_num));
				ratings.push(parseInt(rating.value));
			}
		} catch (error) {
			console.error('Failed to query selector:', error);
		}
	});

	$.ajax({
		type: 'POST',
		url: contextPath + 'preference/submit',
		traditional: true,
		data: {
			song_num: song_nums,
			rating: ratings
		},
		success: function(response) {
			console.log(song_nums);
			console.log(ratings);
			console.log('저장 완료', response);
		},
		error: function(error) {
			console.error('저장 실패', error);
		}
	});
}

document.addEventListener('DOMContentLoaded', function() {
	// 노래 검색
	function searchSongs(page) {
		if(!page){
			page=1
		}
		console.log('Fetching data for page22:', page);

		var searchType = document.getElementById('searchType').value;
		var searchInput = document.getElementById('searchInput').value;

		fetch(`/bridge/preference/search?page=${page}`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				searchType: searchType,
				searchInput: searchInput,
				page: page,
			}),
		})
			.then(response => response.json())
			.then(data => {
				console.log('Received data:', data);
				updateSearchResults(data.songList);
				var totalPages = data.totalPages;
				console.log("Updating pagination. Current Page: " + currentPage + " Total Pages: " + totalPages);
				updatePagination(currentPage, totalPages);
			})
			.catch(error => console.error('Error:', error));
	}


	existingSongsContainer.addEventListener('change', function(event) {
		if (event.target.name === 'selectedSongs') {
			var ratingDiv = event.target.closest('.song-container').querySelector('.rate');
			if (event.target.checked) {
				ratingDiv.style.display = 'block';
			} else {
				ratingDiv.style.display = 'none';
			}
		}
	});


	// 검색 결과 출력
	function updateSearchResults(songs) {
		console.log('Received songs:', songs);

		var existingSongsContainer = document.getElementById('existingSongsContainer');
		existingSongsContainer.innerHTML = '';

		songs.forEach(function(song) {
			var starsHtml = generateStarRadios(song.song_num, song.rating);

			var songHtml = `
            <div class="song-container" style="margin-bottom: 15px;">
                <h5 style="display: flex;">
                    <input type="checkbox" name="selectedSongs" style = "margin-bottom : 10px]"value="${song.song_num}" id="song-${song.song_num}">
                    <label for="song-${song.song_num}">${song.song_name} - ${song.singer}</label>
                    <div class="rate" style="display: none; padding-left: 10px;">
                        ${starsHtml}
                    </div>
                </h5>
            </div>
        `;

			existingSongsContainer.insertAdjacentHTML('beforeend', songHtml);

			// 체크박스 상태에 따라 별점 표시
			var checkbox = existingSongsContainer.querySelector(`#song-${song.song_num}`);
			var ratingDiv = existingSongsContainer.querySelector(`#song-${song.song_num}`).closest('.song-container').querySelector('.rate');
			if (checkbox.checked) {
				ratingDiv.style.display = 'block';
			} else {
				ratingDiv.style.display = 'none';
			}
		});

		// 검색 후 별점 생성 함수
		function generateStarRadios(songNum, initialRating) {
			var starsHtml = '';
			for (var starNum = 5; starNum >= 1; starNum--) {
				var checked = (starNum === initialRating) ? 'checked' : '';
				starsHtml += `
                <input type="radio" id="rating${starNum}-${songNum}" name="rating-${songNum}" value="${starNum}" ${checked}>
                <label for="rating${starNum}-${songNum}" title="${starNum}점"></label>
            `;
			}
			return starsHtml;
		}
	}

	// 페이징
	function createPageLink(pageNumber, text) {
		var pageLink = document.createElement('a');
		pageLink.className = 'page-link';
		pageLink.href = '/preference?page=' + pageNumber;
		pageLink.innerText = text;
		pageLink.dataset.page = pageNumber;
		return pageLink;
	}

	// 페이징 버튼
	function updatePagination(currentPage, totalPages) {
		var paginationContainer = document.getElementById('paginationContainer');
		paginationContainer.innerHTML = '';

		// First and Previous buttons
		if (currentPage > 1) {
			paginationContainer.appendChild(createPageLink(1, 'First'));
			paginationContainer.appendChild(createPageLink(currentPage - 1, 'Previous'));
		}

		// Numbered pages
		var startPage = Math.max(1, currentPage - 5);
		var endPage = Math.min(totalPages, startPage + 9);

		for (var pageNumber = startPage; pageNumber <= endPage; pageNumber++) {
			paginationContainer.appendChild(createPageLink(pageNumber, pageNumber));
		}

		// Next and Last buttons
		if (currentPage + 10 <= totalPages) {
			paginationContainer.appendChild(createPageLink(currentPage + 10, 'Next'));
			paginationContainer.appendChild(createPageLink(totalPages, 'Last'));
		}

		// Attach event listeners to the newly created page links
		var pageLinks = paginationContainer.querySelectorAll('.page-link');
		pageLinks.forEach(function(pageLink) {
			pageLink.addEventListener('click', function(event) {
				event.preventDefault();
				console.log('Event Target:', event.target);

				currentPage = parseInt(event.target.dataset.page);
				console.log('Clicked Page:', currentPage);
				console.log('Updated Current Page:', currentPage);
				searchSongs(currentPage);
			});
		});
	}


	// 폼 제출 방지 및 검색 함수 호출
	document.getElementById('searchForm').addEventListener('submit', function(event) {
		event.preventDefault(); // 폼 제출 방지
		searchSongs(); // 검색 함수 호출
	});

	// 체크박스 상태에 따라 별점 표시
	var checkboxes = document.querySelectorAll('input[name="selectedSongs"]');
	checkboxes.forEach(function(checkbox) {
		checkbox.addEventListener('change', function() {
			var ratingDiv = this.closest('.song-container').querySelector('.rate');
			if (this.checked) {
				ratingDiv.style.display = 'block';
			} else {
				ratingDiv.style.display = 'none';
			}
		});
	});
});
