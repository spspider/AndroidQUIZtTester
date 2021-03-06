package com.example.ttester_paukov.Utils;/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.ttester_paukov.Search_Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A concrete BaseAdapter that is backed by an array of arbitrary
 * objects.  By default this class expects that the provided resource id references
 * a single TextView.  If you want to use a more complex layout, use the constructors that
 * also takes a field id.  That field id should reference a TextView in the larger layout
 * resource.
 *
 * <p>However the TextView is referenced, it will be filled with the toString() of each object in
 * the array. You can add lists or arrays of custom objects. Override the toString() method
 * of your objects to determine what text will be displayed for the item in the list.
 *
 * <p>To use something other than TextViews for the array display, for instance, ImageViews,
 * or to have some of data besides toString() results fill the views,
 * override {@link #getView(int, View, ViewGroup)} to return the type of view you want.
 */
public class ArrayAdapter<T> extends BaseAdapter implements Filterable {
    /**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    private List<T> mObjects;

    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     */
    private final Object mLock = new Object();

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter.
     */
    private int mResource;

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter in a drop down widget.
     */
    private int mDropDownResource;

    /**
     * If the inflated resource is not a TextView, {@link #mFieldId} is used to find
     * a TextView inside the inflated views hierarchy. This field must contain the
     * identifier that matches the one defined in the resource file.
     */
    private int mFieldId = 0;

    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
     * {@link #mObjects} is modified.
     */
    private boolean mNotifyOnChange = true;

    private Context mContext;

    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    private ArrayList<T> mOriginalValues;
    private ArrayFilter mFilter;

    private LayoutInflater mInflater;
    private String mSearchText; // this var for highlight
    HashMap<Integer, Integer> startPos = new HashMap<>();
    HashMap<Integer, Integer> endPos = new HashMap<>();
    String spanString = null;
    private List<Question> questionList;
    private ArrayList<AllThemes> themelist;

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                           instantiating views.
     */
    public ArrayAdapter(Context context, int textViewResourceId) {
        init(context, textViewResourceId, 0, new ArrayList<T>());
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     */
    public ArrayAdapter(Context context, int resource, int textViewResourceId) {
        init(context, resource, textViewResourceId, new ArrayList<T>());
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                           instantiating views.
     * @param objects            The objects to represent in the ListView.
     */
    public ArrayAdapter(Context context, int textViewResourceId, T[] objects) {
        init(context, textViewResourceId, 0, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects            The objects to represent in the ListView.
     */
    public ArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        init(context, resource, textViewResourceId, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                           instantiating views.
     * @param objects            The objects to represent in the ListView.
     */
    public ArrayAdapter(Context context, int textViewResourceId, List<T> objects) {
        init(context, textViewResourceId, 0, objects);
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects            The objects to represent in the ListView.
     */
    public ArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        init(context, resource, textViewResourceId, objects);
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(object);
            } else {
                mObjects.add(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    public void addAll(Collection<? extends T> collection) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(collection);
            } else {
                mObjects.addAll(collection);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    public void addAll(T... items) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.addAll(mOriginalValues, items);
            } else {
                Collections.addAll(mObjects, items);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    public void insert(T object, int index) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            } else {
                mObjects.add(index, object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(object);
            } else {
                mObjects.remove(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            } else {
                mObjects.clear();
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     *                   in this adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator);
            } else {
                Collections.sort(mObjects, comparator);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    /**
     * Control whether methods that change the list ({@link #add},
     * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
     * {@link #notifyDataSetChanged}.  If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     * <p>
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     *                       automatically call {@link
     *                       #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    private void init(Context context, int resource, int textViewResourceId, List<T> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        mObjects = objects;
        mFieldId = textViewResourceId;
    }

    /**
     * Returns the context associated with this array adapter. The context is used
     * to create views from the resource passed to the constructor.
     *
     * @return The Context associated with this adapter.
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * {@inheritDoc}
     */
    public int getCount() {
        return mObjects.size();
    }

    /**
     * {@inheritDoc}
     */
    public T getItem(int position) {
        return mObjects.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    /**
     * {@inheritDoc}
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * {@inheritDoc}
     */


    private View createViewFromResource(int position, View convertView, ViewGroup parent,
                                        int resource) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        T item = getItem(position);
        if (item instanceof CharSequence) {
            text.setText((CharSequence) item);
        } else {
            text.setText(item.toString());
        }

        return view;
    }

    /**
     * <p>Sets the layout resource to create the drop down views.</p>
     *
     * @param resource the layout resource defining the drop down views
     * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
     */
    public void setDropDownViewResource(int resource) {
        this.mDropDownResource = resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    /**
     * Creates a new ArrayAdapter from external resources. The content of the array is
     * obtained through {@link android.content.res.Resources#getTextArray(int)}.
     *
     * @param context        The application's environment.
     * @param textArrayResId The identifier of the array to use as the data source.
     * @param textViewResId  The identifier of the layout used to create views.
     * @return An ArrayAdapter<CharSequence>.
     */
    public static ArrayAdapter<CharSequence> createFromResource(Context context,
                                                                int textArrayResId, int textViewResId) {
        CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
        return new ArrayAdapter<CharSequence>(context, textViewResId, strings);
    }

    /**
     * {@inheritDoc}
     */
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    public void setSpanString(String spanString) {
        this.spanString = spanString;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public void setThemeList(ArrayList<AllThemes> themeList) {
        this.themelist =themeList;
    }

    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<T>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                mSearchText = "";
                ArrayList<T> list;
                synchronized (mLock) {
                    list = new ArrayList<T>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();
                mSearchText = prefixString;
                ArrayList<T> values;
                synchronized (mLock) {
                    values = new ArrayList<T>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<T> newValues = new ArrayList<T>();

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    String valueText = value.toString().toLowerCase();

                    valueText = value.toString().toLowerCase().replace('\r', ' ').replace('\n', ' ');

// First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                        //startPos.put(0,0);
                        startPos.put(0, 0);
                        endPos.put(0, prefixString.length());
                        //endPos.put(0,valueText.length());
                    } else {
                        // Break the prefix into "words"
                        final String[] prefixes = prefixString.split(" ");
                        final int prefixCount = prefixes.length;
                        // HERE CHANGE
                        if (prefixCount > 0) {
                            int loc;
                            int end;
                            // Find the first "word" in prefix
                            if (valueText.contains(prefixes[0]) || (loc = valueText.indexOf(' ' + prefixes[0])) > -1)
                                loc = valueText.indexOf(prefixes[0]) + 1;
                            end = loc + prefixes.length;
                            // Find the following "words" in order
                            int j;
                            for (j = 1; j < prefixCount && loc > -1; j++) {
                                loc = valueText.indexOf(' ' + prefixes[j], loc + 2);
                                end = loc + prefixes[j].length() + 1;
                            }

                            // If every "word" is in this row, add it to the results
                            if (loc > -1) {
                                newValues.add(value);
                                startPos.put(j, loc);
                                endPos.put(j, end);
                                //Log.d(LOG_TAG,"startPos"+loc+"endPos"+end);
                            }
                        }
                    }

                }

                results.values = newValues;
                results.count = newValues.size();


            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(mResource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        // HIGHLIGHT...
        //Log.d("my_logs","start:"+startPos);//+"endPos"+endPos);


        //Alist fullText_Alist = (Alist) getItem(position);
        String fullText = (String) getItem(position);

//            String fullText= fullText_Alist.getQuestion();
            if (mSearchText != null && !mSearchText.isEmpty() && (!mSearchText.equals(" "))) {
                Spannable spannable = new SpannableString(fullText);
                String[] searchWords = mSearchText.split(" ");
                int loc;
                if (fullText.contains(searchWords[0]) || (loc = fullText.indexOf(' ' + searchWords[0])) > -1)
                    loc = fullText.indexOf(searchWords[0]);
                for (int j = 0; j < searchWords.length; j++) {
                    String searchWord = searchWords[j];
                    int startPos = fullText.toLowerCase(Locale.US).indexOf(searchWord.toLowerCase(Locale.US));
                    int endPos = startPos + searchWord.length();

                    if (startPos != -1) {
                        ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
                        TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                        spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                text.setText(spannable);
                //text.append("\n");
                //text.append(spannableAnswer);
//            int startPos = fullText.toLowerCase(Locale.US).indexOf(mSearchText);
//            int endPos = startPos + mSearchText.length();
//
//            if (startPos != -1) {
//                Spannable spannable = new SpannableString(fullText);
//                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
//                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
//                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                text.setText(spannable);
//            } else {
//                text.setText(fullText);
//            }
            } else {
                text.setText(fullText);
            }


        if (questionList != null) {

            ArrayList<String> AllAnoterAnswers=new ArrayList<>();
            String RAnswer = null;

            int RA_id = -1;
            for (int i = 0; i < questionList.size(); i++) {
                if (questionList.get(i).getQUESTION().contains(fullText)) {
                    ArrayList<String> AllAnswers = new ArrayList();
                    AllAnswers.add(questionList.get(i).getOPT1());
                    AllAnswers.add(questionList.get(i).getOPT2());
                    AllAnswers.add(questionList.get(i).getOPT3());
                    AllAnswers.add(questionList.get(i).getOPT4());
                    RA_id = questionList.get(i).getANSWER();
                    //RA_id=questionList.get(i).getANSWER();
                    //RAnswer = AllAnswers.get(RA_id);
                    //RAnswer =AllAnswers.get(RA_id)+questionList.get(i).getTHEMEID();
                    RAnswer =AllAnswers.get(RA_id);//+"theme:"+questionList.get(i).getTHEMEID()+"RA:"+RA_id+questionList.get(i).getOPT1();
                    AllAnoterAnswers = AllAnswers;
                    break;
                    //RAnswer = String.valueOf(AllAnswers);
                    //RAnswer= String.valueOf(RA_id);
                    //break;
                }
            }
            if (RAnswer==null) return view;

            //fullText = fullText +"\n"+ RAnswer;
            Utils utils = new Utils();
            ArrayList<SpannableStringBuilder> span = new ArrayList<>();
            span = (utils.setSpan(RAnswer,AllAnoterAnswers ));
            AllAnoterAnswers .clear();
            Spannable spannableAnswer = new SpannableString(RAnswer);
            //spannableAnswer = span.get(RA_id);
            text.append(":\n");
            text.append(spannableAnswer);
        }


        if (spanString != null) {//?????? NGTV

            fullText = (String) getItem(position);
            if (fullText.contains(spanString)) {

                Spannable spannable = new SpannableString(fullText);
                int startPos = fullText.toLowerCase(Locale.US).indexOf(spanString.toLowerCase(Locale.US));
                int endPos = startPos + fullText.length();
                //Log.d(LOG_TAG, "startPos:" + startPos + "endPos:" + endPos);
                if (startPos != -1) {
                    ColorStateList redColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.RED});
                    TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, redColor, null);
                    spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                text.setText(spannable);
            }
        }


        return view;
    }

    private Spannable makeHighlight(String fullText) {
        Spannable spannable = new SpannableString(fullText);
        if (mSearchText != null && !mSearchText.isEmpty()) {

            //String fullText = (String) getItem(position);

            ArrayList<Integer> StartPosArray = new ArrayList<>();
            ArrayList<Integer> EndPosArray = new ArrayList<>();
            //Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (Map.Entry<Integer, Integer> entry : startPos.entrySet()) {
                //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                StartPosArray.add(entry.getValue());
            }
            for (Map.Entry<Integer, Integer> entry : endPos.entrySet()) {
                //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                EndPosArray.add(entry.getValue());
            }
            //Iterator it2 = endPos.entrySet().iterator();

            if ((StartPosArray.size() > 0) && ((EndPosArray.size() > 0))) {
                if (StartPosArray.size() == EndPosArray.size()) {
                    for (int i = 0; i < StartPosArray.size(); i++) {
                        int startPos = StartPosArray.get(i);
                        int endPos = EndPosArray.get(i);

                        if ((startPos != -1) && (startPos < fullText.length()) && (startPos < fullText.length())) {
                            ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
                            TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                            spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }

                    }
                }
            }
            //return spannable;
        }
        return spannable;
    }


}