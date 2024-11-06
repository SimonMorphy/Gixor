# import torch
# import torch.nn as nn
# import numpy as np
# import pandas as pd
# from torch.utils.data import Dataset, DataLoader
# import optuna
# import torch.nn.functional as F
#
#
# class GithubDataset(Dataset):
#     def __init__(self, data, is_train=True):
#         self.data = data
#         self.is_train = is_train
#         if is_train:
#             self.x = self.data[["Total_Stars_Earned", "Total_Commits_2024", "Total_PRs",
#                                 "Total_PRs_Merged", "Total_PRs_Reviewed", "Total_Issues",
#                                 "Contributed_to_last_year"]].values
#             self.y = self.data["score"].values
#             self.x = self.convert_to_numeric(self.x)
#             self.y = self.convert_to_numeric(self.y)
#         else:
#             self.x = self.data[["Total_Stars_Earned", "Total_Commits_2024", "Total_PRs",
#                                 "Total_PRs_Merged", "Total_PRs_Reviewed", "Total_Issues",
#                                 "Contributed_to_last_year"]].values
#             self.x = self.convert_to_numeric(self.x)
#
#     def convert_to_numeric(self, arr):
#         if isinstance(arr, np.ndarray) and arr.ndim == 1:
#             new_arr = []
#             for item in arr:
#                 if isinstance(item, str) and 'k' in item:
#                     value = float(item.replace('k', '')) * 1000
#                     new_arr.append(value)
#                 elif isinstance(item, (int, float)):
#                     new_arr.append(item)
#                 else:
#                     try:
#                         new_arr.append(float(item))
#                     except (TypeError, ValueError):
#                         new_arr.append(0)
#             return np.array(new_arr)
#         elif isinstance(arr, (int, float)):
#             try:
#                 value = float(arr)
#                 if value < 0:
#                     value = 0
#                 elif value > 100:
#                     value = 100
#                 return np.array([value])
#             except (TypeError, ValueError):
#                 return np.array([0])
#         else:
#             new_arr = []
#             for row in arr:
#                 new_row = []
#                 for item in row:
#                     if isinstance(item, str) and 'k' in item:
#                         value = float(item.replace('k', '')) * 1000
#                         if value < 0:
#                             value = 0
#                         elif value > 100:
#                             value = 100
#                         new_row.append(value)
#                     elif isinstance(item, (int, float)):
#                         if item < 0:
#                             item = 0
#                         elif item > 100:
#                             item = 100
#                         new_row.append(item)
#                     else:
#                         try:
#                             value = float(item)
#                             if value < 0:
#                                 value = 0
#                             elif value > 100:
#                                 value = 100
#                             new_row.append(value)
#                         except (TypeError, ValueError):
#                             new_row.append(0)
#                 new_arr.append(new_row)
#             return np.array(new_arr)
#
#     def __len__(self):
#         return len(self.x)
#
#     def __getitem__(self, idx):
#         if self.is_train:
#             return (torch.tensor(self.x[idx], dtype=torch.float32),
#                     torch.tensor(self.y[idx], dtype=torch.float32))
#         else:
#             return torch.tensor(self.x[idx], dtype=torch.float32)
#
#
# class SimpleModel(nn.Module):
#     def __init__(self, hidden_size_1, hidden_size_2):
#         super(SimpleModel, self).__init__()
#         self.fc1 = nn.Linear(7, hidden_size_1)
#         self.fc2 = nn.Linear(hidden_size_1, hidden_size_2)
#         self.fc3 = nn.Linear(hidden_size_2, 1)
#
#     def forward(self, x):
#         x = torch.relu(self.fc1(x))
#         x = torch.relu(self.fc2(x))
#         x = self.fc3(x)
#         x = F.sigmoid(x) * 100  # 将输出映射到0 - 100
#         return x
#
#
# train_data = pd.read_csv("has_score.csv")
# test_data = pd.read_csv("no_score.csv")
# train_dataset = GithubDataset(train_data)
# test_dataset = GithubDataset(test_data, is_train=False)
#
#
# def objective(trial):
#     lr = trial.suggest_float("lr", low=1e-4, high=1e-1, log=True)
#     batch_size = trial.suggest_categorical("batch_size", [16, 32, 64])
#     hidden_size_1 = trial.suggest_int("hidden_size_1", 32, 128)
#     hidden_size_2 = trial.suggest_int("hidden_size_2", 32, 128)
#
#     model = SimpleModel(hidden_size_1, hidden_size_2)
#     criterion = nn.MSELoss()
#     optimizer = torch.optim.Adam(model.parameters(), lr=lr)
#     train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)
#
#     num_epochs = 100
#     for epoch in range(num_epochs):
#         running_loss = 0.0
#         for inputs, labels in train_loader:
#             optimizer.zero_grad()
#             outputs = model(inputs)
#             loss = criterion(outputs.squeeze(), labels)
#             loss.backward()
#             optimizer.step()
#             running_loss += loss.item()
#     return running_loss / len(train_loader)
#
#
# study = optuna.create_study(direction="minimize")
# study.optimize(objective, n_trials=10)
#
# best_params = study.best_params
# best_model = SimpleModel(best_params["hidden_size_1"], best_params["hidden_size_2"])
# criterion = nn.MSELoss()
# optimizer = torch.optim.Adam(best_model.parameters(), lr=best_params["lr"])
# train_loader = DataLoader(train_dataset, batch_size=best_params["batch_size"], shuffle=True)
#
# num_epochs = 100
# for epoch in range(num_epochs):
#     running_loss = 0.0
#     for inputs, labels in train_loader:
#         optimizer.zero_grad()
#         outputs = best_model(inputs)
#         loss = criterion(outputs.squeeze(), labels)
#         loss.backward()
#         optimizer.step()
#         running_loss += loss.item()
#     print(f"Epoch {epoch + 1}, Loss: {running_loss / len(train_loader)}")
#
# best_model.eval()
# with torch.no_grad():
#     test_x = test_dataset.x
#     test_tensor = torch.tensor(test_x, dtype=torch.float32)
#     predictions = best_model(test_tensor).numpy().flatten()
# test_data["score"] = predictions
# test_data.to_csv("data.csv", index=False)
# data.csv
import torch
import torch.nn as nn
import numpy as np
import pandas as pd
from torch.utils.data import Dataset, DataLoader
import optuna
import torch.nn.functional as F
import copy


class GithubDataset(Dataset):
    def __init__(self, data, is_train=True):
        self.data = data
        self.is_train = is_train
        if is_train:
            self.x = self.data[["Total_Stars_Earned", "Total_Commits_2024", "Total_PRs",
                                "Total_PRs_Merged", "Total_PRs_Reviewed", "Total_Issues",
                                "Contributed_to_last_year"]].values
            self.y = self.data["score"].values
            self.x = self.convert_to_numeric(self.x)
            self.y = self.convert_to_numeric(self.y)
        else:
            self.x = self.data[["Total_Stars_Earned", "Total_Commits_2024", "Total_PRs",
                                "Total_PRs_Merged", "Total_PRs_Reviewed", "Total_Issues",
                                "Contributed_to_last_year"]].values
            self.x = self.convert_to_numeric(self.x)

    def convert_to_numeric(self, arr):
        if isinstance(arr, np.ndarray) and arr.ndim == 1:
            new_arr = []
            for item in arr:
                if isinstance(item, str) and 'k' in item:
                    value = float(item.replace('k', '')) * 1000
                    new_arr.append(value)
                elif isinstance(item, (int, float)):
                    new_arr.append(item)
                else:
                    try:
                        new_arr.append(float(item))
                    except (TypeError, ValueError):
                        new_arr.append(0)
            return np.array(new_arr)
        elif isinstance(arr, (int, float)):
            try:
                value = float(arr)
                if value < 0:
                    value = 0
                elif value > 100:
                    value = 100
                return np.array([value])
            except (TypeError, ValueError):
                return np.array([0])
        else:
            new_arr = []
            for row in arr:
                new_row = []
                for item in row:
                    if isinstance(item, str) and 'k' in item:
                        value = float(item.replace('k', '')) * 1000
                        if value < 0:
                            value = 0
                        elif value > 100:
                            value = 100
                        new_row.append(value)
                    elif isinstance(item, (int, float)):
                        if item < 0:
                            item = 0
                        elif item > 100:
                            item = 100
                        new_row.append(item)
                    else:
                        try:
                            value = float(item)
                            if value < 0:
                                value = 0
                            elif value > 100:
                                value = 100
                            new_row.append(value)
                        except (TypeError, ValueError):
                            new_row.append(0)
                new_arr.append(new_row)
            return np.array(new_arr)

    def __len__(self):
        return len(self.x)

    def __getitem__(self, idx):
        if self.is_train:
            return (torch.tensor(self.x[idx], dtype=torch.float32),
                    torch.tensor(self.y[idx], dtype=torch.float32))
        else:
            return torch.tensor(self.x[idx], dtype=torch.float32)


class SimpleModel(nn.Module):
    def __init__(self, hidden_size_1, hidden_size_2):
        super(SimpleModel, self).__init__()
        self.fc1 = nn.Linear(7, hidden_size_1)
        self.fc2 = nn.Linear(hidden_size_1, hidden_size_2)
        self.fc3 = nn.Linear(hidden_size_2, 1)

    def forward(self, x):
        x = torch.relu(self.fc1(x))
        x = torch.relu(self.fc2(x))
        x = self.fc3(x)
        x = F.sigmoid(x) * 100
        return x


train_data = pd.read_csv("has_score.csv")
test_data = pd.read_csv("no_score.csv")
train_dataset = GithubDataset(train_data)
test_dataset = GithubDataset(test_data, is_train=False)


def objective(trial):
    lr = trial.suggest_float("lr", low=1e-4, high=1e-1,log=True)
    batch_size = trial.suggest_categorical("batch_size", [16, 32, 64])
    hidden_size_1 = trial.suggest_int("hidden_size_1", 32, 128)
    hidden_size_2 = trial.suggest_int("hidden_size_2", 32, 128)

    model = SimpleModel(hidden_size_1, hidden_size_2)
    criterion = nn.MSELoss()
    optimizer = torch.optim.Adam(model.parameters(), lr=lr)
    train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)

    num_epochs = 100
    for epoch in range(num_epochs):
        running_loss = 0.0
        for inputs, labels in train_loader:
            optimizer.zero_grad()
            outputs = model(inputs)
            loss = criterion(outputs.squeeze(), labels)
            loss.backward()
            optimizer.step()
            running_loss += loss.item()
    return running_loss / len(train_loader)


study = optuna.create_study(direction="minimize")
study.optimize(objective, n_trials=10)

best_params = study.best_params


# Bagging集成学习
n_models = 5
models = []
for _ in range(n_models):
    train_data_indices = np.random.choice(len(train_dataset), size=len(train_dataset), replace=True)
    sub_train_dataset = torch.utils.data.Subset(train_dataset, train_data_indices)
    sub_train_loader = DataLoader(sub_train_dataset, batch_size=32, shuffle=True)
    model = SimpleModel(hidden_size_1=best_params["hidden_size_1"], hidden_size_2=best_params["hidden_size_2"])
    criterion = nn.MSELoss()
    optimizer = torch.optim.Adam(model.parameters(), lr=best_params["lr"])
    for epoch in range(100):
        running_loss = 0.0
        for inputs, labels in sub_train_loader:
            optimizer.zero_grad()
            outputs = model(inputs)
            loss = criterion(outputs.squeeze(), labels)
            loss.backward()
            optimizer.step()
            running_loss += loss.item()
    models.append(model)

test_x = test_dataset.x
test_tensor = torch.tensor(test_x, dtype=torch.float32)
predictions = []
for model in models:
    model.eval()
    with torch.no_grad():
        output = model(test_tensor).numpy().flatten()
        predictions.append(output)
predictions = np.array(predictions)
final_predictions = np.mean(predictions, axis=0)
test_data["score"] = final_predictions
test_data.to_csv("data2.csv", index=False)