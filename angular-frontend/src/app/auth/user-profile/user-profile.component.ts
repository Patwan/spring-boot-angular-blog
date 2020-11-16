import { Component, OnInit } from '@angular/core';
import { PostService } from 'src/app/shared/post.service';
import { ActivatedRoute } from '@angular/router';
import { CommentService } from 'src/app/comment/comment.service';
import { PostModel } from 'src/app/shared/post-model';
import { CommentPayload } from 'src/app/comment/comment.payload';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  name: string;
  posts: PostModel[];
  comments: CommentPayload[];
  postLength: number;
  commentLength: number;

  //Once this class is instantiated we capture the name from the active route
  //and store is a variable called name (whose type is String) we then pass
  //this variable to getAllPostsByUser inside postService method (this method
  //fetches all the posts from the backend of this particular user), and once 
  //it gets a response, it returns an observable which we subscribe/listen to 
  //and assign the response in posts array and also the posts length. We also
  //fetch the commednts via passing in the name
  constructor(private activatedRoute: ActivatedRoute, 
              private postService: PostService,
              private commentService: CommentService) {
    this.name = this.activatedRoute.snapshot.params.name;

    this.postService.getAllPostsByUser(this.name).subscribe(data => {
      this.posts = data;
      this.postLength = data.length;
    });
    this.commentService.getAllCommentsByUser(this.name).subscribe(data => {
      this.comments = data;
      this.commentLength = data.length;
    });
  }

  ngOnInit(): void {
  }

}
