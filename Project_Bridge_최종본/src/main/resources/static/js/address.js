// 폼 제출 방지 및 검색 함수 호출
document.getElementById('contactForm').addEventListener('submit', function(event) {
	event.preventDefault(); // 폼 제출 방지
	var address = document.getElementById('search_address').value;
	console.log(address);
	search_address(address); // 검색 함수 호출
});

function search_address(address) {
	var formData = new FormData();
	formData.append('search_address', address);

	fetch(`/bridge/matchingPage/getSelectedAddress`, {
		method: 'POST',
		body: formData,
	})
		.then(response => response.json())
		.then(data => {
			console.log('Received data:', data);
		})
		.catch(error => console.error('Error:', error));
}