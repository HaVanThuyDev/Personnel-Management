
document.addEventListener("DOMContentLoaded", function() {
    // Line chart cho VN-Index
    const ctx = document.getElementById('stockChart').getContext('2d');
    const stockChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
            datasets: [{
                label: 'VN-Index',
                data: [1120, 1132, 1115, 1140, 1150, 1138, 1162],
                backgroundColor: 'rgba(123, 97, 255, 0.2)',
                borderColor: 'rgba(123, 97, 255, 1)',
                borderWidth: 2,
                tension: 0.4,
                pointRadius: 4,
                pointHoverRadius: 6,
                fill: true
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: true
                }
            },
            scales: {
                y: {
                    beginAtZero: false,
                    ticks: {
                        callback: function(value) {
                            return value.toLocaleString('vi-VN');
                        }
                    }
                }
            }
        }
    });
    const ctxBar = document.getElementById('stockBarChart').getContext('2d');
    const stockBarChart = new Chart(ctxBar, {
        type: 'bar',
        data: {
            labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
            datasets: [{
                label: 'VN-Index - Bar',
                data: [1120, 1132, 1115, 1140, 1150, 1138, 1162],
                backgroundColor: 'rgba(255, 193, 7, 0.6)',
                borderColor: 'rgba(255, 193, 7, 1)',
                borderWidth: 1,
                borderRadius: 6,
                maxBarThickness: 38
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: true
                }
            },
            scales: {
                y: {
                    beginAtZero: false,
                    ticks: {
                        callback: function(value) {
                            return value.toLocaleString('vi-VN');
                        }
                    }
                }
            }
        }
    });
});
document.addEventListener('DOMContentLoaded', function () {
    const formInput = document.querySelector('.form-input');
    if (!formInput) return;

    // Tạo phần tử SVG sóng
    const waveWrapper = document.createElement('div');
    waveWrapper.style.position = 'absolute';
    waveWrapper.style.left = '0';
    waveWrapper.style.right = '0';
    waveWrapper.style.bottom = '0';
    waveWrapper.style.top = '0';
    waveWrapper.style.zIndex = '0';
    waveWrapper.style.pointerEvents = 'none';
    waveWrapper.style.overflow = 'hidden';

    const svgNS = "http://www.w3.org/2000/svg";
    const svg = document.createElementNS(svgNS, "svg");
    svg.setAttribute("width", "100%");
    svg.setAttribute("height", "100%");
    svg.setAttribute("viewBox", "0 0 320 48");
    svg.style.display = "block";
    svg.style.position = "absolute";
    svg.style.left = "0";
    svg.style.bottom = "0";
    svg.style.width = "100%";
    svg.style.height = "60px";
    svg.style.zIndex = "1";
    const path = document.createElementNS(svgNS, "path");
    path.setAttribute("fill", "url(#wave-gradient)");
    path.setAttribute("fill-opacity", "0.58");
    path.setAttribute("d", wavePath(0));
    const defs = document.createElementNS(svgNS, "defs");
    const linearGradient = document.createElementNS(svgNS, "linearGradient");
    linearGradient.setAttribute("id", "wave-gradient");
    linearGradient.setAttribute("x1", "0%");
    linearGradient.setAttribute("x2", "100%");
    linearGradient.setAttribute("y1", "0%");
    linearGradient.setAttribute("y2", "100%");
    const stop1 = document.createElementNS(svgNS, "stop");
    stop1.setAttribute("offset", "0%");
    stop1.setAttribute("stop-color", "#00c3ff");
    const stop2 = document.createElementNS(svgNS, "stop");
    stop2.setAttribute("offset", "100%");
    stop2.setAttribute("stop-color", "#005bea");
    linearGradient.appendChild(stop1);
    linearGradient.appendChild(stop2);
    defs.appendChild(linearGradient);
    svg.appendChild(defs);
    svg.appendChild(path);
    waveWrapper.appendChild(svg);
    formInput.appendChild(waveWrapper);
    formInput.style.position = "relative";
    formInput.style.zIndex = "2";
    let waveOffset = 0;
    function animateWave() {
        waveOffset += 0.045;
        path.setAttribute("d", wavePath(waveOffset));
        requestAnimationFrame(animateWave);
    }
    animateWave();
    function wavePath(offset) {
        const width = 320, height = 48, amplitude = 8, frequency = 2.2;
        let d = `M0,${height}`;
        for (let x = 0; x <= width; x += 8) {
            const y = height - 16 - Math.sin((x / width) * frequency * Math.PI * 2 + offset) * amplitude;
            d += ` L${x},${y.toFixed(2)}`;
        }
        d += ` L${width},${height} Z`;
        return d;
    }
});
document.addEventListener('DOMContentLoaded', function () {
    const profile = document.querySelector('.profile');
    if (!profile) return;
    const helloBox = document.createElement('div');
    helloBox.textContent = "Xin chào bạn!";
    helloBox.style.position = 'absolute';
    helloBox.style.top = '-55px';
    helloBox.style.left = '50%';
    helloBox.style.transform = 'translateX(-50%) scale(0.96)';
    helloBox.style.padding = '11px 26px 13px 26px';
    helloBox.style.borderRadius = '18px';
    helloBox.style.background = 'linear-gradient(100deg,#f0f7ff 50%,#a1c4fd 100%)';
    helloBox.style.color = '#3561ad';
    helloBox.style.fontWeight = 'bold';
    helloBox.style.fontSize = '1.14rem';
    helloBox.style.boxShadow = '0 4px 18px rgba(123,97,255,0.13)';
    helloBox.style.opacity = '0';
    helloBox.style.pointerEvents = 'none';
    helloBox.style.zIndex = '10';
    helloBox.style.transition = 'opacity 0.22s, transform 0.22s';
    helloBox.style.textAlign = 'center';
    helloBox.style.whiteSpace = 'nowrap';
    const triangle = document.createElement('div');
    triangle.style.position = 'absolute';
    triangle.style.left = '50%';
    triangle.style.bottom = '-13px';
    triangle.style.transform = 'translateX(-50%)';
    triangle.style.width = '0';
    triangle.style.height = '0';
    triangle.style.borderLeft = '13px solid transparent';
    triangle.style.borderRight = '13px solid transparent';
    triangle.style.borderTop = '13px solid #a1c4fd';
    helloBox.appendChild(triangle);

    profile.style.position = 'relative';
    profile.appendChild(helloBox);
    function showHello() {
        helloBox.style.opacity = '1';
        helloBox.style.transform = 'translateX(-50%) scale(1.03)';
    }
    function hideHello() {
        helloBox.style.opacity = '0';
        helloBox.style.transform = 'translateX(-50%) scale(0.96)';
    }

    profile.addEventListener('mouseenter', showHello);
    profile.addEventListener('mouseleave', hideHello);
    profile.addEventListener('focus', showHello);
    profile.addEventListener('blur', hideHello);
    profile.addEventListener('click', function(e) {
        showHello();
        setTimeout(hideHello, 1650);
        e.preventDefault();
    });
});