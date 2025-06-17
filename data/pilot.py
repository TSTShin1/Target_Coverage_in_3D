import numpy as np
import matplotlib.pyplot as plt

# Bộ dữ liệu
data = np.array([3, 3, 4, 5, 6, 5, 4, 3, 5, 5])

# Tính giá trị trung bình và độ lệch chuẩn
mean_value = np.mean(data)
std_dev = np.std(data, ddof=1)  # ddof=1 để tính độ lệch chuẩn mẫu

# Vẽ biểu đồ
plt.figure(figsize=(6, 4))
plt.bar(1, mean_value, yerr=std_dev, capsize=10, color='skyblue', alpha=0.7)

# Định dạng biểu đồ
plt.xticks([1], ["Mean"])
plt.ylabel("Value")
plt.title("Biểu đồ độ lệch chuẩn của bộ dữ liệu")
plt.grid(axis='y', linestyle='--', alpha=0.6)

# Hiển thị biểu đồ
plt.show()
