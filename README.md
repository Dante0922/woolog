# woolog

## 비공개, 공개 여부 (상태값) -> (Enum)

## 카테고리 -> DB(or Enum)

## 로그인 -> spring security

## 비밀번호 암호화
1. 해시
2. 해시 방식
   1. SHA1
   2. SHA@256
   3. MD5
   4. 이런 걸로 암호화 하면 안 되는 이유
3. BCrypt SCrypt, Argon2
   1. salt 값

## 댓글의 API 구조

게시글
POST /posts
GET /posts/{postId}

댓글
POST /comments?postId=1 
POST /posts/{postId}/comments
- 아래와 같은 경우가 생길 수도?
POST /posts/{postId}/category/{categoryId}/comments


DELETE /posts/{postId}/comments/{commentId}
- 아래처럼 수정
DELETE /comments/{commentId}
PATCH /comments/{commentId}