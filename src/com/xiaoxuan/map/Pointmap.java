package com.xiaoxuan.map;
import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Point;

//�ҵ���Сֵ���ڵ�λ��
//������ԭ����bug�����ҵ�һ��bug,���ܴ�(0,0)����������(0,0)(1,0)ѭ��

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
        path[countp]=terminal;                  //path�е�һ����Ϊ�յ�
        while(path[0]!=start){
        	for(int i=countp;i>=0;i=i-1){
        		path[i+1]=path[i];              //path��ÿһ���������һλ
        	}
        	path[0]=recall[path[1]];            //path�е�һ������ȻΪ���µĸ��ڵ�  
        	countp=countp+1;	                //������������λ
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
        //���ߺ�����point.x*500+point.y����  
        int start=initial.x*N2+initial.y;
        int terminal=end.x*N2+end.y;
        int count=N1*N2;
        
        //�����ͼ�ϵľ������
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

	    //��Ϊ��barrier�д��ڵĵ�����ܱ���չ
        /*ArrayList<Point> barrier=new ArrayList<Point>();
	    barrier.add(new Point(0,1));
	    barrier.add(new Point(1,1));
    	barrier.add(new Point(2,1));  */
        
        ArrayList<Point> barrier=barrierPoints.getBarrierPoint();
	    
    	//��ʼ����������
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
        int[] recall=new int[count];     //�����û����ҵ�ÿ����ĸ��ڵ� 
        int countc=0;                    //��¼CLOSED���Ԫ������
        int counto=0;                    //��¼OPEN���Ԫ������
        //����ѭ��
         while(OPEN!=null){
        	double[] f_temp=new double[OPEN.length];
        	for(int i=0;i<OPEN.length;i=i+1){
        		f_temp[i]=f[OPEN[i]];
        	}
        	//ȡfΪ��С�ĵ����CLOSED��
        	Searchmin min=new Searchmin();
        	int index;
        	index=min.getindex(f_temp);
        	int BEST=OPEN[index];
        	CLOSED[countc]=OPEN[index];           //�ҵ��ĵ����CLOSED��
        	countc=countc+1;
        	
        	for(int i=index+1;i<OPEN.length;i=i+1){
        		OPEN[i-1]=OPEN[i];                  
        	}                                    //��OPEN�б���ɾȥ
        	counto=counto-1;
        	if(BEST==terminal){
        		break;
        	}
        	//����Ѱ�Һ�̽ڵ㣺��Ϊÿ��ֻ����һ��������չ��Χ��8����
        	int mark=BEST-(N2+1);
        	int lx=(int)(Math.floor(mark/N2));
        	int ly=mark%N2;       	
        	if(mark>=0&&mark<count&&ly!=N2-1){
        		Judgempty test1=new Judgempty();
        		if(A[BEST][mark]!=Double.MAX_VALUE && A[BEST][mark]!=0 && test1.judgempty(barrier,(new Point(lx,ly)))!=true){ //�������ϰ��㣬����ɵ���
        			int flag=0;                               //��־�õ��Ƿ��Ѿ���OPEN��CLOSED��
        			double temp=g[BEST]+A[BEST][mark];        //��������չ����ѵ㵽��õ�ľ���
        			 
        			if(test1.judgempty(OPEN, mark)){    //�����OPEN��
        				if(temp<g[mark]){
        					g[mark]=temp;
        					recall[mark]=BEST;                //���������ѵ��ٵ��õ�ľ����ֱ�ӵ��õ�̣��þ�����ѵ�ľ������ԭ���ľ���
        				}
        				flag=1;                               //�����Ѿ���OPEN��
        			}
        			
        			if(test1.judgempty(CLOSED, mark)){          //�����CLOSED��
        				if(temp<g[mark]){                     //��������ѵ��ֱ�ӵ��õ��
        					g[mark]=temp;
        					recall[mark]=BEST;                //���ڵ�Ϊ��ѵ�
        					counto=counto+1; 
        					OPEN[counto]=mark;
        					CLOSED[countc]=0;                 //���õ��CLOSED��ɾ��������OPEN�� 
        					countc=countc-1;
        				}
        				flag=1;                              //�����Ѿ���CLOSED��
        			}
        			if(flag==0){                             //�Ȳ���OPEN�ֲ���CLOSED
        				recall[mark]=BEST;
        				counto=counto+1;
    					OPEN[counto]=mark;                   //���ڵ�Ϊ��ѵ㣬����OPEN��      
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
		//����astar�㷨�õ���·��
		return (ArrayList<Point>) this.path.clone();
	}
    
}
