document.addEventListener("DOMContentLoaded", function () {
    const containerbarNav = document.querySelector(".container-barnav-");
    const bars = 28;
    const maxAnimationDuration = 8000; // 최대 애니메이션 지속 시간
    const minAnimationDuration =7000; // 최소 애니메이션 지속 시간
    const barColor = "#74b9ff"; // 바의 색상

    for (let i = 0; i < bars; i++) {
        const barbarNav = document.createElement("div");
        barbarNav.classList.add("bar-bar-nav");
        barbarNav.style.width = "5px";
        barbarNav.style.minHeight = "20px";
        barbarNav.style.marginLeft = "1px";
        barbarNav.style.boxShadow = "0 0px 0px #000";
        barbarNav.style.backgroundColor = barColor;
        
        barbarNav.style.opacity = "0.7";
        barbarNav.style.borderRadius = "20px";

        const animationDuration = Math.floor(Math.random() * (maxAnimationDuration - minAnimationDuration + 1)) + minAnimationDuration;
        barbarNav.style.animation = `grow${i} ${animationDuration}ms alternate infinite`;

        containerbarNav.appendChild(barbarNav);

        const keyframes = document.styleSheets[0].insertRule(`
            @keyframes grow${i} {
                ${Array.from({ length: 11 }, (_, j) => `
                    ${j * 10}% {
                        height: ${Math.floor(Math.random() * 65)}px;
                    }
                `).join("\n")}
            }
        `, document.styleSheets[0].cssRules.length);
    }
});