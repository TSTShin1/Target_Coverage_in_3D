import os
from sklearn.datasets import make_blobs
import numpy as np

output_path = os.path.join('C:\\Users\\Admin\\Desktop\\vscode_java\\DE_Sensor-main\\data\\sensor.input')
# Tạo tọa độ cho 300 sensor, chia thành 15 cụm trong không gian 3D 100x100x100
n_samples = 300
n_features = 3
n_clusters = 8
sensor_radius = 40

centers = np.random.uniform(0, 100, (n_clusters, n_features))  # Tạo trung tâm các cụm trong khoảng từ 0 đến 100

cluster_std = 5
sensors, _ = make_blobs(n_samples=n_samples, centers=centers, n_features=n_features, cluster_std=cluster_std)

# Clip giá trị tọa độ để đảm bảo nằm trong không gian 100x100x100
sensors = np.clip(sensors, 0, 100)

sensors = np.round(sensors).astype(int)

# Ghi dữ liệu vào file sensor.input
with open(output_path, 'w') as f:
    for sensor in sensors:
        x, y, z = sensor
        f.write(f"{x} {y} {z} {sensor_radius}\n")

print("Sensor data has been written to sensor.input")
