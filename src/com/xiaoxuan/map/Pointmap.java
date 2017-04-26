package com.xiaoxuan.map;
import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Point;

//找到最小值所在的位置
//调好了原来的bug但又找到一个bug,不能从(0,0)出发，会在(0,0)(1,0)循环

class Searchmin{
public int getindex(double[] x){
		 int index=0;
		 double temp=x[0];
		
		 for(int i=1;i<x.length;i=i+1){
			 if(x[i]<temp){
				 temp=x[i];
				 index=i;
			 }
		 }
		 return index;
	 }
}
class Judgempty{
	public boolean judgempty(int[] x,int y){
	    for(int i=0;i<x.length;i=i+1){
	    	if(y==x[i]){
	    		return true;
	    	}
	    }	    
	    return false;
	}
	public boolean judgempty(ArrayList<Point> a,Point b){
		if(a.contains(b)){
			return true;
		}
		else{
			return false;
		}
	    }
}
class Showpath{
	private ArrayList<Point> store=new ArrayList<Point>();
	public void showpath(int[] recall,int start,int terminal,int count,int N2){
        int[] path=new int[count];
        int countp=0;
        path[countp]=terminal;                  //path中第一个点为终点
        while(path[0]!=start){
        	for(int i=countp;i>=0;i=i-1){
        		path[i+1]=path[i];              //path中每一个点向后移一位
        	}
        	path[0]=recall[path[1]];            //path中第一个点仍然为最新的父节点  
        	countp=countp+1;	                //计数，方便移位
        }
        for(int i=0;i<=countp;i=i+1){
        	int a=(int)(Math.floor(path[i]/N2));
        	int b=path[i]%N2;
        	Point pathpoint=new Point(a,b);
//        	System.out.println(pathpoint.x+" "+pathpoint.y+" ");
        }   
	}
	public ArrayList<Point> getpath(){
		return (ArrayList<Point>) this.store.clone();
	}
}

public class Pointmap {
	private ArrayList<Point> path;
	public Pointmap(Barrier barrierPoints, Point initial,Point end){
        int N1=25;
        int N2=50;
        //两者后面用point.x*500+point.y代替  
        int start=initial.x*N2+initial.y;
        int terminal=end.x*N2+end.y;
        int count=N1*N2;
        
        //构造地图上的距离矩阵
        double A[][]=new double[count][count];
        for(int i=0;i<count;i=i+1){
        for (int j=0;j<count;j=j+1){
        		double i1=Math.floor(i/N2);
        		double j1=i%N2;
        		double i2=Math.floor(j/N2);
        		double j2=j%N2;
        		if(Math.abs(i1-i2)==1 && Math.abs(j1-j2)==1){
        			A[i][j]=Math.sqrt(2.0);
        		}else{
        			A[i][j]=Math.abs(i1-i2)+Math.abs(j1-j2);
        		}		
        }        
        }

	    //认为在barrier中存在的点均不能被扩展
        /*ArrayList<Point> barrier=new ArrayList<Point>();
	    barrier.add(new Point(0,1));
	    barrier.add(new Point(1,1));
    	barrier.add(new Point(2,1));  */
        
        ArrayList<Point> barrier=barrierPoints.getBarrierPoint();
	    
    	//初始化启发函数
        double dist[][] = new double[A.length][A[0].length];
        System.arraycopy(A,0,dist,0,A.length);
        int[] OPEN=new int[count];
        int[] CLOSED=new int[count];
        OPEN[0]=start;
        double[] f=new double[count];
        for(int i=0;i<count;i=i+1){
        	f[i]=Double.MAX_VALUE;
        }
        double g[] = new double[f.length];
        System.arraycopy(f,0,g,0,f.length);
        f[start]=0+dist[start][terminal];
        g[start]=0;
        int[] recall=new int[count];     //便于用回溯找到每个点的父节点 
        int countc=0;                    //记录CLOSED里的元素数量
        int counto=0;                    //记录OPEN里的元素数量
        //进入循环
         while(OPEN!=null){
        	double[] f_temp=new double[OPEN.length];
        	for(int i=0;i<OPEN.length;i=i+1){
        		f_temp[i]=f[OPEN[i]];
        	}
        	//取f为最小的点加入CLOSED中
        	Searchmin min=new Searchmin();
        	int index;
        	index=min.getindex(f_temp);
        	int BEST=OPEN[index];
        	CLOSED[countc]=OPEN[index];           //找到的点加入CLOSED中
        	countc=countc+1;
        	
        	for(int i=index+1;i<OPEN.length;i=i+1){
        		OPEN[i-1]=OPEN[i];                  
        	}                                    //从OPEN列表中删去
        	counto=counto-1;
        	if(BEST==terminal){
        		break;
        	}
        	//继续寻找后继节点：因为每次只能走一格，所以扩展周围的8个点
        	int mark=BEST-(N2+1);
        	int lx=(int)(Math.floor(mark/N2));
        	int ly=mark%N2;       	
        	if(mark>=0&&mark<count&&ly!=N2-1){
        		Judgempty test1=new Judgempty();
        		if(A[BEST][mark]!=Double.MAX_VALUE && A[BEST][mark]!=0 && test1.judgempty(barrier,(new Point(lx,ly)))!=true){ //不能是障碍点，必须可到达
        			int flag=0;                               //标志该点是否已经在OPEN、CLOSED中
        			double temp=g[BEST]+A[BEST][mark];        //经过已扩展的最佳点到达该点的距离
        			 
        			if(test1.judgempty(OPEN, mark)){    //如果在OPEN中
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;                //如果经历最佳点再到该点的距离比直接到该点短，用经历最佳点的距离替代原来的距离
        				}
        				flag=1;                               //表明已经在OPEN中
        			}
        			
        			if(test1.judgempty(CLOSED, mark)){          //如果在CLOSED中
        				if(temp<g[mark]){                     //但经历最佳点比直接到该点短
        					g[mark]=temp;
        					recall[mark]=BEST;                //父节点为最佳点
        					counto=counto+1; 
        					OPEN[counto]=mark;
        					CLOSED[countc]=0;                 //将该点从CLOSED中删除，加入OPEN中 
        					countc=countc-1;
        				}
        				flag=1;                              //表明已经在CLOSED中
        			}
        			if(flag==0){                             //既不在OPEN又不在CLOSED
        				recall[mark]=BEST;
        				counto=counto+1;
    					OPEN[counto]=mark;                   //父节点为最佳点，加入OPEN中      
    					g[mark]=temp;
    					f[mark]=g[mark]+dist[mark][terminal];
        			}
        		}
        	}
        	mark=BEST-1;
        	lx=(int)(Math.floor(mark/N2));
        	ly=mark%N2;
        	if(mark>=0&&mark<count&&ly!=N2-1){
        		Judgempty test2=new Judgempty();
        		if(A[BEST][mark]!=Double.MAX_VALUE && A[BEST][mark]!=0 && test2.judgempty(barrier, new Point(lx,ly))!=true){
        			int flag=0;
        			double temp=g[BEST]+A[BEST][mark];
        			if(test2.judgempty(OPEN, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        				}
        				flag=1;
        			}
        			if(test2.judgempty(CLOSED, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        					counto=counto+1;
        					OPEN[counto]=mark;
        					CLOSED[countc]=0;
        					countc=countc-1;
        				}
        				flag=1;
        			}
        			if(flag==0){
        				recall[mark]=BEST;
        				counto=counto+1;
    					OPEN[counto]=mark;
    					g[mark]=temp;
    					f[mark]=g[mark]+dist[mark][terminal];
        			}
        		}
        	}
        	mark=BEST+(N2-1);
        	lx=(int)(Math.floor(mark/N2));
        	ly=mark%N2;
        	if(mark>=0&&mark<count&&ly!=N2-1){
        		Judgempty test3=new Judgempty();
        		if(A[BEST][mark]!=Double.MAX_VALUE && A[BEST][mark]!=0 && test3.judgempty(barrier, new Point(lx,ly))!=true){
        			int flag=0;
        			double temp=g[BEST]+A[BEST][mark];
        			
        			if(test3.judgempty(OPEN, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        				}
        				flag=1;
        			}
        			if(test3.judgempty(CLOSED, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        					counto=counto+1;
        					OPEN[counto]=mark;
        					CLOSED[countc]=0;
        					countc=countc-1;
        				}
        				flag=1;
        			}
        			if(flag==0){
        				recall[mark]=BEST;
        				counto=counto+1;
    					OPEN[counto]=mark;
    					g[mark]=temp;
    					f[mark]=g[mark]+dist[mark][terminal];
        			}
        		}
        	}
        	mark=BEST-N2;
        	lx=(int)(Math.floor(mark/N2));
        	ly=mark%N2;
        	if(mark>=0&&mark<count){
        		Judgempty test4=new Judgempty();
        		if(A[BEST][mark]!=Double.MAX_VALUE && A[BEST][mark]!=0 &&test4.judgempty(barrier, new Point(lx,ly))!=true){
        			int flag=0;
        			double temp=g[BEST]+A[BEST][mark];       			
        			if(test4.judgempty(OPEN, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        				}
        				flag=1;
        			}
        			if(test4.judgempty(CLOSED, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        					counto=counto+1;
        					OPEN[counto]=mark;
        					CLOSED[countc]=0;
        					countc=countc-1;
        				}
        				flag=1;
        			}
        			if(flag==0){
        				recall[mark]=BEST;
        				counto=counto+1;
    					OPEN[counto]=mark;
    					g[mark]=temp;
    					f[mark]=g[mark]+dist[mark][terminal];
        			}
        		}
        	}
        	mark=BEST+N2;
        	lx=(int)(Math.floor(mark/N2));
        	ly=mark%N2;
        	if(mark>=0&&mark<count){
        		Judgempty test5=new Judgempty();
        		if(A[BEST][mark]!=Double.MAX_VALUE && A[BEST][mark]!=0 && test5.judgempty(barrier, new Point(lx,ly))!=true){
        			int flag=0;
        			double temp=g[BEST]+A[BEST][mark];
        			if(test5.judgempty(OPEN, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        				}
        				flag=1;
        			}
        			if(test5.judgempty(CLOSED, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        					counto=counto+1;
        					OPEN[counto]=mark;
        					CLOSED[countc]=0;
        					countc=countc-1;
        				}
        				flag=1;
        			}
        			if(flag==0){
        				recall[mark]=BEST;
        				counto=counto+1;
    					OPEN[counto]=mark;
    					g[mark]=temp;
    					f[mark]=g[mark]+dist[mark][terminal];
        			}
        		}
        	}
        	mark=BEST-(N2-1);
        	lx=(int)(Math.floor(mark/N2));
        	ly=mark%N2;
        	if(mark>=0&&mark<count&&ly!=0){
        		Judgempty test6=new Judgempty();
        		if(A[BEST][mark]!=Double.MAX_VALUE && A[BEST][mark]!=0 && test6.judgempty(barrier,new Point(lx,ly))!=true){
        			int flag=0;
        			double temp=g[BEST]+A[BEST][mark];
        			if(test6.judgempty(OPEN, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        				}
        				flag=1;
        			}
        			if(test6.judgempty(CLOSED, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        					counto=counto+1;
        					OPEN[counto]=mark;
        					CLOSED[countc]=0;
        					countc=countc-1;
        				}
        				flag=1;
        			}
        			if(flag==0){
        				recall[mark]=BEST;
        				counto=counto+1;
    					OPEN[counto]=mark;
    					g[mark]=temp;
    					f[mark]=g[mark]+dist[mark][terminal];
        			}
        		}
        	}
        	mark=BEST+1;
        	lx=(int)(Math.floor(mark/N2));
        	ly=mark%N2;
        	if(mark>=0&&mark<count&&ly!=0){
        		Judgempty test7=new Judgempty();
        		if(A[BEST][mark]!=Double.MAX_VALUE && A[BEST][mark]!=0 && test7.judgempty(barrier,new Point(lx,ly))!=true){
        			int flag=0;
        			double temp=g[BEST]+A[BEST][mark];
        			if(test7.judgempty(OPEN, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        				}
        				flag=1;
        			}
        			if(test7.judgempty(CLOSED, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        					counto=counto+1;
        					OPEN[counto]=mark;
        					CLOSED[countc]=0;
        					countc=countc-1;
        				}
        				flag=1;
        			}
        			if(flag==0){
        				recall[mark]=BEST;
        				counto=counto+1;
    					OPEN[counto]=mark;
    					g[mark]=temp;
    					f[mark]=g[mark]+dist[mark][terminal];
        			}
        		}
        	}
        	mark=BEST+N2+1;
        	lx=(int)(Math.floor(mark/N2));
        	ly=mark%N2;
        	if(mark>=0&&mark<count&&ly!=0){
        		Judgempty test8=new Judgempty();
        		if(A[BEST][mark]!=Double.MAX_VALUE && A[BEST][mark]!=0 && test8.judgempty(barrier,new Point(lx,ly))!=true){
        			int flag=0;
        			double temp=g[BEST]+A[BEST][mark];
        			if(test8.judgempty(OPEN, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        				}
        				flag=1;
        			}
        			if(test8.judgempty(CLOSED, mark)){
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;
        					counto=counto+1;
        					OPEN[counto]=mark;
        					CLOSED[countc]=0;
        					countc=countc-1;
        				}
        				flag=1;
        			}
        			if(flag==0){
        				recall[mark]=BEST;
        				counto=counto+1;
    					OPEN[counto]=mark;
    					g[mark]=temp;
    					f[mark]=g[mark]+dist[mark][terminal];
        			}
        		}
        	}
        }         
     Showpath route=new Showpath();
     route.showpath(recall, start, terminal, count, N2);
     this.path = route.getpath();
    }
	
	
	public ArrayList<Point> getPath(){
		//返回astar算法得到的路径
		return (ArrayList<Point>) this.path.clone();
	}
    
}
