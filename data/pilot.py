import matplotlib.pyplot as plt
from sklearn.datasets import make_blobs

# Tạo ra các điểm dữ liệu (sensor)
X, y = make_blobs(n_samples=300, centers=2, n_features=2, cluster_std=5, center_box=(0, 100), random_state=42)

# Vẽ biểu đồ phân tán của các điểm dữ liệu
plt.figure(figsize=(8, 8))
plt.scatter(X[:, 0], X[:, 1], c=y, cmap='viridis', marker='o', s=50)
plt.xlim(0, 100)
plt.ylim(0, 100)
plt.title('Sensor Distribution in 2D Space (100x100)')
plt.xlabel('X-coordinate')
plt.ylabel('Y-coordinate')
plt.grid(True)
plt.show()
