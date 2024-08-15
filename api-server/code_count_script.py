import os
import subprocess
from datetime import datetime

# Git 리포지토리 루트 디렉토리로 이동
# os.chdir("/path/to/your/git/repo")

# 출력 파일 지정
output_file = "code_count.txt"

# 출력 파일 초기화
with open(output_file, 'w') as f:
    pass

# 텍스트 파일 리스트 가져오기
result = subprocess.run(
    ["git", "ls-tree", "-r", "HEAD"],
    capture_output=True,
    text=True
)
files = [line.split("\t")[1] for line in result.stdout.splitlines()]

# 텍스트 파일 필터링
text_files = []
for file in files:
    result = subprocess.run(["file", file], capture_output=True, text=True)
    if "text" in result.stdout:
        text_files.append(file)

# 작성자 정보와 커밋 시간 정보를 텍스트 파일로 저장
author_commit_lines = {}

with open(output_file, 'a') as f:
    for filename in text_files:
        f.write(f"Processing {filename}...\n")
        result = subprocess.run(
            ["git", "blame", "--line-porcelain", filename],
            capture_output=True,
            text=True
        )
        lines = result.stdout.splitlines()
        author = None
        commit_time = None
        for line in lines:
            if line.startswith("author "):
                author = line.split(" ", 1)[1]
            if line.startswith("committer-time "):
                timestamp = int(line.split(" ", 1)[1])
                commit_time = datetime.fromtimestamp(timestamp).strftime("%Y-%m-%d %H:%M:%S")
            if line.startswith("filename "):
                if author and commit_time:
                    key = (author, commit_time[:10])  # 날짜까지만 비교하기 위해 시간 부분을 제거
                    if key not in author_commit_lines:
                        author_commit_lines[key] = 0
                    author_commit_lines[key] += 1
                    author = None
                    commit_time = None

# 결과를 날짜 순으로 정렬하여 출력 파일에 저장
sorted_author_commit_lines = sorted(author_commit_lines.items(), key=lambda x: x[0][1])

with open(output_file, 'a') as f:
    for (author, date), line_count in sorted_author_commit_lines:
        if author == '박현민':
            f.write(f"Author: {author}, Date: {date}, Lines: {line_count}\n")
