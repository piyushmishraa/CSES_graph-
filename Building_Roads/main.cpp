#include <bits/stdc++.h>
using namespace std;

unordered_map<int, vector<int>> adj;
vector<int> visited;

void dfs(int source) {
  visited[source] = 1;
  for (int neigh : adj[source]) {
    if (!visited[neigh]) {
      dfs(neigh);
    }
  }
}

int main() {
  int n, m;
  cin >> n >> m;

  vector<pair<int, int>> connections(m);
  for (int i = 0; i < m; i++) {
    int a, b;
    cin >> a >> b;
    connections[i] = {a, b};
  }

  for (int i = 0; i < m; i++) {
    int u = connections[i].first;
    int v = connections[i].second;
    adj[u].push_back(v);
    adj[v].push_back(u);
  }

  visited.assign(n + 1, 0);
  vector<int> reps;

  for (int city = 1; city <= n; city++) {
    if (!visited[city]) {
      dfs(city);
      reps.push_back(city);
    }
  }

  cout << reps.size() - 1 << endl;
  for (int i = 0; i < (int)reps.size() - 1; i++) {
    cout << reps[i] << " " << reps[i + 1] << endl;
  }

  return 0;
}
