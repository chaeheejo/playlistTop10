# playlist_top10

### Description
> 사용자들의 플레이리스트 공유 앱   
 
<br>

### function
+ 사용자들이 기본 정보를 입력하고 회원가입을 한다.
+ 사용자들은 자신의 플레이리스트를 등록하고, 수정할 수 있다.
+ 사용자들은 다른 사용자의 플레이리스트에 좋아요 표시를 할 수 있다.
+ 자신이 좋아요 누른 사용자의 리스트를 확인할 수 있다.
+ 전체 사용자들의 플레이리스트는 각 사용자가 받은 좋아요 순으로 보여진다.

<br>

### development 
+ MVVM 패턴을 사용해 View - ViewModel - UserRepository 순으로 계층이 구성되어 있다.
+ compose를 사용해 리스트를 화면에 보여주었다.
+ RecyclerView를 사용해 리스트를 띄워주는 본래의 방식 또한 사용히였다.
+ NoSQL DB로 firebase를 사용하였다.
