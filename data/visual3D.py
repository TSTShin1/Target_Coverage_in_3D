import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import numpy as np

plt.rcParams.update({
    "text.usetex": True
})

def read_sensor_file(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()
        sensors = [list(map(float, line.split())) for line in lines]
    return sensors

def read_target_file(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()
        targets = [list(map(float, line.split())) for line in lines]
    return targets

def plot_sensors_targets(sensors, targets):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')

    # Vẽ các điểm sensor trước
    sensor_scatter = ax.scatter([x for x, y, z, radius in sensors],
                                [y for x, y, z, radius in sensors],
                                [z for x, y, z, radius in sensors],
                                c='green', marker='o', label='Sensor', alpha=1, s=20)

    # Vẽ các điểm target sau cùng với kích thước lớn hơn và màu sắc đậm hơn
    target_scatter = ax.scatter([x for x, y, z in targets],
                                [y for x, y, z in targets],
                                [z for x, y, z in targets],
                                c='red', marker='x', label='Target', alpha=1, s=40)

    # Thêm chú thích (legend)
    ax.legend(loc='upper left')

    ax.set_xlabel('L-axis')
    ax.set_ylabel('W-axis')
    ax.set_zlabel('D-axis')
    plt.title('Distribution of Sensors and Targets in UWSNs')
    plt.show()

# Thay đổi đường dẫn tới file sensor.input và target.input của bạn
sensor_file_path = 'C:\\Users\\Admin\\Desktop\\vscode_java\\DE_Sensor-main\\data\\sensor.input'
target_file_path = 'C:\\Users\\Admin\\Desktop\\vscode_java\\DE_Sensor-main\\data\\sentarget.input'

# Đọc dữ liệu từ file
sensors = read_sensor_file(sensor_file_path)
targets = read_target_file(target_file_path)

# Vẽ hiển thị
plot_sensors_targets(sensors, targets)
