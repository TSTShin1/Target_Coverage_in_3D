import matplotlib.pyplot as plt
import numpy as np

plt.rcParams.update({
    "text.usetex": True
})

def read_data(filepath):
    with open(filepath, 'r') as file:
        lines = file.readlines()

    generations = []
    values = []

    for line in lines:
        if line.startswith("Best fit for generation"):
            parts = line.split(":")
            generation = int(r" ".join(parts[0].split(" ")[4:]))
            value = (float)(parts[1].strip())
            generations.append(generation)
            values.append(value)

    return generations, values

def filter_every_nth_point(generations, values, n=2):
    return generations[::n], values[::n]

# csfont = {'fontname':''}

# Đọc dữ liệu từ file genDE.out
# gen_de_filepath = 'result\\genDE.out'
# generations_de, values_de = read_data(gen_de_filepath)
# generations_de_filtered, values_de_filtered = filter_every_nth_point(generations_de, values_de)

gen_der_filepath = 'result\\timeDE.out'
generations_der, values_der = read_data(gen_der_filepath)
generations_der_filtered, values_der_filtered = filter_every_nth_point(generations_der, values_der)

# gen_ga_filepath = 'result\\genGA.out'
# generations_ga, values_ga = read_data(gen_ga_filepath)
# generations_ga_filtered, values_ga_filtered = filter_every_nth_point(generations_ga, values_ga)

# Đọc dữ liệu từ file genGA.out
gen_gar_filepath = 'result\\timeGA.out'
generations_gar, values_gar = read_data(gen_gar_filepath)
generations_gar_filtered, values_gar_filtered = filter_every_nth_point(generations_gar, values_gar)


# # Đọc dữ liệu từ file genHMS.out
# gen_hms_filepath = 'result\\genHMS.out'
# generations_hms, values_hms = read_data(gen_hms_filepath)
# generations_hms_filtered, values_hms_filtered = filter_every_nth_point(generations_hms, values_hms)

gen_hmsr_filepath = 'result\\timeHMS.out'
generations_hmsr, values_hmsr = read_data(gen_hmsr_filepath)
generations_hmsr_filtered, values_hmsr_filtered = filter_every_nth_point(generations_hmsr, values_hmsr)

# gen_mphsa_filepath = 'result\\genMPHSA.out'
# generations_mphsa, values_mphsa = read_data(gen_mphsa_filepath)
# generations_mphsa_filtered, values_mphsa_filtered = filter_every_nth_point(generations_mphsa, values_mphsa)

gen_mphsar_filepath = 'result\\timeMPHSA.out'
generations_mphsar, values_mphsar = read_data(gen_mphsar_filepath)
generations_mphsar_filtered, values_mphsar_filtered = filter_every_nth_point(generations_mphsar, values_mphsar)


# Vẽ đồ thị liền mạch
# plt.plot(generations_de_filtered, values_de_filtered, label='DE with $TH = 0.8$', marker='.', linestyle='-')
plt.plot(generations_der_filtered, values_der_filtered, label='DE', marker='x', linestyle='-')
# plt.plot(generations_ga_filtered, values_ga_filtered, label='GA with $TH = 0.8$', marker='.', linestyle='-')
plt.plot(generations_gar_filtered, values_gar_filtered, label='GA', marker='.', linestyle='-')
# plt.plot(generations_hms_filtered, values_hms_filtered, label='HMS with $TH = 0.8$', marker='.', linestyle='-')
plt.plot(generations_hmsr_filtered, values_hmsr_filtered, label='HMS', marker='v', linestyle='-')
# plt.plot(generations_mphsa_filtered, values_mphsa_filtered, label='MPHSA with $TH = 0.8$', marker='.', linestyle='-')
plt.plot(generations_mphsar_filtered, values_mphsar_filtered, label='MPHSA', marker='o', linestyle='-')


plt.title('Comparison of four algorithms DE, GA, HMS and MPHSA')
plt.xlabel('Key time')
plt.ylabel('Total time')
plt.grid(True)
plt.legend(bbox_to_anchor=(1, 0.3), loc='right', borderaxespad=0)



# Lưu đồ thị thành file ảnh (ví dụ: PNG)
plt.savefig('result//Graph2.png')


