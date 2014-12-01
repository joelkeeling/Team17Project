package com.ualberta.team17.test;

import java.util.Date;

import com.ualberta.team17.QAModel;
import com.ualberta.team17.CommentItem;
import com.ualberta.team17.UniqueId;
import com.ualberta.team17.view.IQAView;

import junit.framework.TestCase;

public class CommentItemTest extends TestCase {
	private CommentItem testComment;
	boolean notified;
	int notifyCount;
	public void setUp() {
		testComment = new CommentItem( new UniqueId(), new UniqueId(), "author", new Date(), "body", 0);
		notified = false;
		notifyCount = 0;
	}
	
	/**
	 * Tests that comment upvoting works properly.
	 */
	public void test_CI1_Upvote()
	{
		assertEquals("No upvotes", testComment.getUpvoteCount(), 0);
		
		testComment.upvote();
		
		assertEquals("One upvote", testComment.getUpvoteCount(), 1);
	}
	
	/**
	 * Tests that views are notified on upvote.
	 */
	public void test_CI2_UpvoteNotifiesView()
	{
		IQAView dummyView = new IQAView() {
			public void update(QAModel model) {
				notified = true;
			}
		};
		testComment.addView(dummyView);
		testComment.upvote();		
		assertTrue( "View was notified", notified );			
	}
	
	/**
	 * Tests that views removed from the observer list are not notified.
	 */
	public void test_CI3_DeleteView()
	{		
		IQAView dummyView = new IQAView() {
			public void update(QAModel model) {
				notified = true;
			}
		};
		testComment.addView(dummyView);
		testComment.deleteView(dummyView); 
		testComment.upvote();		
		assertFalse("No views were nofified", notified);		
	}
	
	/**
	 * Tests that all views are notified on update.
	 */
	public void test_CI4_NotifyViews()
	{		
		IQAView dummyViewA = new IQAView() {
			public void update(QAModel model) {
				notifyCount++;
			}
		};
		IQAView dummyViewB = new IQAView() {
			public void update(QAModel model) {
				notifyCount++;
			}
		};
		IQAView dummyViewC = new IQAView() {
			public void update(QAModel model) {
				notifyCount++;
			}
		};
		
		testComment.addView(dummyViewA);
		testComment.addView(dummyViewB);
		testComment.addView(dummyViewC);
		testComment.notifyViews();
		assertEquals("All views were notified", notifyCount, 3);
	}
}
