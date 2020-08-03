package com.example.ogenna_esimai_simple_to_do;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Responsible for displaying data from the model into a row in the RecyclerView
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    // adapter is parameterized by the ViewHolder. That's why it's impt to define the ViewHolder first (b4 the Adapter)
    // class ViewHolder extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    public interface OnClickListener {
        void onItemClicked(int position);
    }
    public interface OnLongClickListener {
        void onItemLongClicked (int position); //4) passed in position parameter to this method so that
        // the class in MainActivity which is implementing the onItemLongClicked method knows the
        // position where we did the LongClick so it can notify the Adapter that that is the position
        // where we should delete
    }


    List<String> Items; //defining variable we can use in all the other methods
    // 6) we'll make a new member variable
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public ItemsAdapter(List<String> Items, OnLongClickListener longClickListener, OnClickListener clickListener) { //constructor
        this.Items = Items; //setting member var = to var passed in thru the const
        // 7)
        this.longClickListener = longClickListener;

        // 5) modify this constructor for ItemsAdapter so we can take in an OnLongClickListener
        // public ItemsAdapter(List<String> Items) { //constructor  //to the left is the original constructor

        this.clickListener = clickListener;
    }

    @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //use layout inflator to inflate a view
            View todoView =  LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false );
            //wrap it inside a ViewHolder and return it
            return new ViewHolder(todoView);
        }

        //responsible for binding data (at a particular position) to a particular ViewHolder
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            //grab the item at the position (given the position u wanna get the corresponding item)
            String item = Items.get(position);
            //bind the item into the specified ViewHolder
            holder.bind(item);
        }

        //tells the RV how many items are in the list
        @Override
        public int getItemCount() {
            return Items.size();
        }

        //Container to provide easy access to views that represent each row of the list
        class ViewHolder extends RecyclerView.ViewHolder {

            //using the info from resource layout file simple_list_item_1.xml used above in public
            //class ItemsAdapter > public ViewHolder onCreateViewHolder, in simple_list_item_1.xml there's
            //only one TextView with id text1 which is the specific info that will be used now to reference
            //to that TextView
            TextView tvItem;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvItem = itemView.findViewById(android.R.id.text1);
            }

            //update the view inside of the ViewHolder with the data of String item
            //do this by getting a reference to the view that we can actually access in bind(). See TextView above
            public void bind(String item) {
                tvItem.setText(item);
                tvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onItemClicked(getAdapterPosition());
                    }
                });
                //2) back from MainActivity.java. remove logic from list by pressing and holding. remove any item from the list(long holding) on that item
                tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                    public boolean onLongClick(View view) {
                        // 10) comment 3) is no longer correct at this point. -> Notify the listener
                        // which position was long pressed. Back to MainActivity

                        //3) remove the item from the recycler view. The difficulty is that we don't
                        // an ability to talk to the adapter located behind the recycler view at this point.
                        //what we need to do is communicate that this particular item in the recycler view
                        //was clicked. And to communicate that back to MainActivity(.java).
                        //So somehow, we need to pass info from MainActivity into the ItemsAdapter(.java).
                        //we do this by creating an interface in ItemsAdapter(.java) that MainActivity will implement

                        // 8) now that we have this variable longClickListener, we can invoke the
                        // method onItemLongClicked on this class (pointing to ViewHolder class) from
                        // the OnLongClickListener on the text view

                        longClickListener.onItemLongClicked(getAdapterPosition()); //pass position of where this (public)
                        // ViewHolder is. We do that by
                        return true; // 9) changed false to true meaning that the callback is consuming the LongClick
                    }
                        });
            }
        }
}

//1) now that our Adapter is implemented, next we want to use it in our MainActivity.java. We head on over there